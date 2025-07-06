package io.github.protocol.pulsar.admin.jdk;

import org.junit.jupiter.api.Test;

public class BrokersTest extends BaseTest{

    @Test
    public void testHealthCheckV1() throws PulsarAdminException {
        PulsarAdmin pulsarAdmin = PulsarAdmin.builder().port(getPort()).build();
        pulsarAdmin.brokers().healthcheck(TopicVersion.V1);
    }

    @Test
    public void testHealthCheckV2() throws PulsarAdminException {
        PulsarAdmin pulsarAdmin = PulsarAdmin.builder().port(getPort()).build();
        pulsarAdmin.brokers().healthcheck(TopicVersion.V2);
    }
}
