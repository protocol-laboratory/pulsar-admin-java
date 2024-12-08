package io.github.protocol.pulsar.admin.jdk;

import io.github.embedded.pulsar.core.EmbeddedPulsarServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class BrokersTest {
    private static final EmbeddedPulsarServer SERVER = new EmbeddedPulsarServer();

    @BeforeAll
    public static void setup() throws Exception {
        SERVER.start();
    }

    @AfterAll
    public static void teardown() throws Exception {
        SERVER.close();
    }

    @Test
    public void testHealthCheckV1() throws PulsarAdminException {
        PulsarAdmin pulsarAdmin = PulsarAdmin.builder().port(SERVER.getWebPort()).build();
        pulsarAdmin.brokers().healthcheck(TopicVersion.V1);
    }

    @Test
    public void testHealthCheckV2() throws PulsarAdminException {
        PulsarAdmin pulsarAdmin = PulsarAdmin.builder().port(SERVER.getWebPort()).build();
        pulsarAdmin.brokers().healthcheck(TopicVersion.V2);
    }
}
