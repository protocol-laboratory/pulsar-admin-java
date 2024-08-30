package io.github.protocol.pulsar.admin.jdk;

import java.net.http.HttpResponse;

public class Brokers {
    private final InnerHttpClient innerHttpClient;

    public Brokers(InnerHttpClient innerHttpClient) {
        this.innerHttpClient = innerHttpClient;
    }

    public void healthcheck(TopicVersion topicVersion) throws PulsarAdminException {
        String url = UrlConst.HEALTHCHECK;
        if (topicVersion != null) {
            url += "?topicVersion=" + topicVersion;
        }
        try {
            HttpResponse<String> httpResponse = innerHttpClient.get(url);
            if (httpResponse.statusCode() != 200) {
                throw new PulsarAdminException("healthcheck failed, status code: " + httpResponse.statusCode(),
                        httpResponse.statusCode());
            }
            if (!httpResponse.body().equals("ok")) {
                throw new PulsarAdminException("healthcheck failed, body: " + httpResponse.body());
            }
        } catch (Exception e) {
            throw new PulsarAdminException(e);
        }
    }
}
