package io.github.protocol.pulsar.admin.jdk;

public class PersistentTopics extends BaseTopicsImpl {

    private static final String BASE_URL_PERSISTENT_DOMAIN = "/admin/v2" + "/persistent";

    public PersistentTopics(InnerHttpClient httpClient) {
        super(httpClient);
    }

    public String getDomainBaseUrl() {
        return BASE_URL_PERSISTENT_DOMAIN;
    }
}
