package io.github.protocol.pulsar.admin.jdk;

import java.util.List;

public interface Clusters {

    List<String> getClusters() throws PulsarAdminException;

}
