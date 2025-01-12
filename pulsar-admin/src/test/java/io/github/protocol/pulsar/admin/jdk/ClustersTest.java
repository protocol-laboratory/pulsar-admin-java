package io.github.protocol.pulsar.admin.jdk;

import io.github.embedded.pulsar.core.EmbeddedPulsarServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class ClustersTest {

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
    public void getClustersTest() throws PulsarAdminException {
        Assertions.assertEquals(Arrays.asList("standalone"),
                PulsarAdmin.builder().port(SERVER.getWebPort()).build().clusters().getClusters());
    }

}
