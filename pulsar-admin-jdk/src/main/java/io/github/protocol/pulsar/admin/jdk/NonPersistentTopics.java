package io.github.protocol.pulsar.admin.jdk;

public class NonPersistentTopics extends BaseTopicsImpl {

    private static final String BASE_URL_NON_PERSISTENT_DOMAIN = "/admin/v2" + "/non-persistent";

    public NonPersistentTopics(InnerHttpClient httpClient) {
        super(httpClient);
    }

    @Override
    public String getDomainBaseUrl() {
        return BASE_URL_NON_PERSISTENT_DOMAIN;
    }

}
