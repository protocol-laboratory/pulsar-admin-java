package io.github.protocol.pulsar.admin.jdk;

import io.github.embedded.pulsar.core.EmbeddedPulsarServer;
import io.github.openfacade.http.HttpClientConfig;
import io.github.openfacade.http.HttpClientEngine;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.provider.Arguments;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {
    protected EmbeddedPulsarServer server;
    protected List<PulsarAdmin> pulsarAdmins;

    @BeforeAll
    public void setup() throws Exception {
        server = new EmbeddedPulsarServer();
        server.start();
        pulsarAdmins = initPulsarAdmins();
    }

    @AfterAll
    public void teardown() throws Exception {
        server.close();
    }

    protected int getPort() {
        return server.getWebPort();
    }

    protected List<PulsarAdmin> initPulsarAdmins() {
        List<PulsarAdmin> pulsarAdmins = new ArrayList<>();
        for (HttpClientEngine engine: HttpClientEngine.values()) {
            // async and jetty client has dependency conflicts with embedded pulsar core, skip it.
            if (engine.equals(HttpClientEngine.Async) || engine.equals(HttpClientEngine.Jetty)) {
                continue;
            }
            PulsarAdminBuilder pulsarAdminBuilder = PulsarAdmin.builder();
            pulsarAdminBuilder.host("localhost");
            pulsarAdminBuilder.port(getPort());
            HttpClientConfig.Builder clientBuilder = new HttpClientConfig.Builder();
            clientBuilder.engine(engine);
            pulsarAdminBuilder.httpClientConfig(clientBuilder.build());
            pulsarAdmins.add(pulsarAdminBuilder.build());
        }
        return pulsarAdmins;
    }

    protected Stream<Arguments> providePulsarAdmins() {
        return pulsarAdmins.stream().map(pulsarAdmin -> Arguments.arguments(pulsarAdmin));
    }
}
