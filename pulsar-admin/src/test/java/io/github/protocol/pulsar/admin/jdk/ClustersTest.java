package io.github.protocol.pulsar.admin.jdk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class ClustersTest extends BaseTest{
    @Test
    public void getClustersTest() throws PulsarAdminException {
        Assertions.assertEquals(Arrays.asList("standalone"),
                PulsarAdmin.builder().port(getPort()).build().clusters().getClusters());
    }

}
