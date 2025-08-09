package io.github.protocol.pulsar.admin.jdk;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class BrokersTest extends BaseTest{

    @MethodSource("providePulsarAdmins")
    @ParameterizedTest
    public void testHealthCheckV1(PulsarAdmin pulsarAdmin) throws PulsarAdminException {
        pulsarAdmin.brokers().healthcheck(TopicVersion.V1);
    }

    @MethodSource("providePulsarAdmins")
    @ParameterizedTest
    public void testHealthCheckV2(PulsarAdmin pulsarAdmin) throws PulsarAdminException {
        pulsarAdmin.brokers().healthcheck(TopicVersion.V2);
    }
}
