package io.github.protocol.pulsar.admin.jdk;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.openfacade.http.HttpResponse;
import io.github.protocol.pulsar.admin.common.JacksonService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PersistentTopics extends BaseTopicsImpl {

    private static final String BASE_URL_PERSISTENT_DOMAIN = "/admin/v2" + "/persistent";

    public PersistentTopics(InnerHttpClient httpClient) {
        super(httpClient);
    }

    public String getDomainBaseUrl() {
        return BASE_URL_PERSISTENT_DOMAIN;
    }

    public void createSubscription(String tenant, String namespace, String encodedTopic, String subscriptionName,
                                   boolean replicated, boolean authoritative, SubscriptionMessageId messageId)
        throws PulsarAdminException {
        String url = String.format("%s/%s/%s/%s/subscription/%s", getDomainBaseUrl(), tenant, namespace, encodedTopic,
            subscriptionName);
        try {
            HttpResponse response =
                httpClient.put(url, messageId, "replicated", String.valueOf(replicated), "authoritative",
                    String.valueOf(authoritative));
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(
                    String.format("failed to create subscription %s for topic %s/%s/%s, status code %s, body : %s",
                        subscriptionName, tenant, namespace, encodedTopic, response.statusCode(),
                        response.bodyAsString()));
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new PulsarAdminException(e);
        }
    }

    public void deleteSubscription(String tenant, String namespace, String encodedTopic, String subName, boolean force,
                                   boolean authoritative) throws PulsarAdminException {
        String url =
            String.format("%s/%s/%s/%s/subscription/%s", getDomainBaseUrl(), tenant, namespace, encodedTopic, subName);
        try {
            HttpResponse response =
                httpClient.delete(url, "force", String.valueOf(force), "authoritative", String.valueOf(authoritative));
            if (response.statusCode() != 204) {
                throw new PulsarAdminException(
                    String.format("failed to delete subscription %s of topic %s/%s/%s, status code %s, body : %s",
                        subName, tenant, namespace, encodedTopic, response.statusCode(), response.bodyAsString()));
            }
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new PulsarAdminException(e);
        }
    }

    public List<String> getSubscriptions(String tenant, String namespace, String encodedTopic, boolean authoritative)
        throws PulsarAdminException {
        String url = String.format("%s/%s/%s/%s/subscriptions", getDomainBaseUrl(), tenant, namespace, encodedTopic);
        try {
            HttpResponse response = httpClient.get(url, "authoritative", String.valueOf(authoritative));
            if (response.statusCode() != 200) {
                throw new PulsarAdminException(
                    String.format("failed to get subscriptions of topic %s/%s/%s, status code %s, body : %s", tenant,
                        namespace, encodedTopic, response.statusCode(), response.bodyAsString()));
            }
            return JacksonService.toRefer(response.body(), new TypeReference<List<String>>() {
            });
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new PulsarAdminException(e);
        }
    }
}
