package io.github.protocol.pulsar;

public class NonPersistentTopicsImpl extends PersistentTopicsImpl {

    private static final String BASE_URL_NON_PERSISTENT_DOMAIN = "/admin/v2" + "/non-persistent";

    public NonPersistentTopicsImpl(InnerHttpClient httpClient) {
        super(httpClient);
    }

    @Override
    public String getDomainBaseUrl() {
        return BASE_URL_NON_PERSISTENT_DOMAIN;
    }

    @Override
    public MessageIdImpl getLastMessageId(String tenant, String namespace, String encodedTopic, boolean authoritative)
            throws PulsarAdminException {
        throw new PulsarAdminException(new UnsupportedOperationException(
                "get last message id on non-persistent topic is not allowed"));
    }
}
