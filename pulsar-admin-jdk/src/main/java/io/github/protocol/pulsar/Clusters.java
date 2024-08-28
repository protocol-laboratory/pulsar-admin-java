package io.github.protocol.pulsar;

import java.util.List;

public interface Clusters {

    List<String> getClusters() throws PulsarAdminException;

}
