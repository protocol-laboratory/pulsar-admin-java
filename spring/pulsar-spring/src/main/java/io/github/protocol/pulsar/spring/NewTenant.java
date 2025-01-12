package io.github.protocol.pulsar.spring;

import lombok.Getter;

@Getter
public class NewTenant {
    String tenant;

    public NewTenant(String tenant) {
        this.tenant = tenant;
    }
}
