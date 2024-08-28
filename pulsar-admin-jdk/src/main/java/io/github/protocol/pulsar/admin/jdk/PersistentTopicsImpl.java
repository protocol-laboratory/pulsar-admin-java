package io.github.protocol.pulsar.admin.jdk;

public class PersistentTopicsImpl extends BaseTopicsImpl {

    private static final String BASE_URL_PERSISTENT_DOMAIN = "/admin/v2" + "/persistent";

    public PersistentTopicsImpl(InnerHttpClient httpClient) {
        super(httpClient);
    }

    public String getDomainBaseUrl() {
        return BASE_URL_PERSISTENT_DOMAIN;
    }
}
