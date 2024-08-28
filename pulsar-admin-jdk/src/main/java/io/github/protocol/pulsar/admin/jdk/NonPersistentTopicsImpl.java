package io.github.protocol.pulsar.admin.jdk;

public class NonPersistentTopicsImpl extends BaseTopicsImpl {

    private static final String BASE_URL_NON_PERSISTENT_DOMAIN = "/admin/v2" + "/non-persistent";

    public NonPersistentTopicsImpl(InnerHttpClient httpClient) {
        super(httpClient);
    }

    @Override
    public String getDomainBaseUrl() {
        return BASE_URL_NON_PERSISTENT_DOMAIN;
    }

}
