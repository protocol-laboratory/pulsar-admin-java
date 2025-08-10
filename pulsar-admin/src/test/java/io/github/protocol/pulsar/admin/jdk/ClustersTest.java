package io.github.protocol.pulsar.admin.jdk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;

public class ClustersTest extends BaseTest{

    @MethodSource("providePulsarAdmins")
    @ParameterizedTest
    public void getClustersTest(PulsarAdmin pulsarAdmin) throws PulsarAdminException {
        Assertions.assertEquals(Arrays.asList("standalone"), pulsarAdmin.clusters().getClusters());
    }
}
