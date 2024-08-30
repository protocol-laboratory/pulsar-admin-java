package io.github.protocol.pulsar.admin.reactive;

import io.github.protocol.pulsar.admin.api.Configuration;
import io.netty.handler.ssl.SslContext;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.HttpClientSecurityUtils;

public class InnerReactiveClient {
    private final HttpClient httpClient;

    private final String httpPrefix;

    public InnerReactiveClient(Configuration conf) {
        HttpClient client = HttpClient.create();

        if (conf.tlsEnabled) {
            client = client.secure(spec -> {
                SslContext context = SslContextUtil.build(conf.tlsConfig);
                if (conf.tlsConfig.hostnameVerifyDisabled) {
                    spec.sslContext(context)
                            .handlerConfigurator(HttpClientSecurityUtils.HOSTNAME_VERIFICATION_CONFIGURER);
                } else {
                    spec.sslContext(context);
                }
            });
            this.httpPrefix = "https://" + conf.host + ":" + conf.port;
        } else {
            this.httpPrefix = "http://" + conf.host + ":" + conf.port;
        }

        this.httpClient = client;
    }
}
