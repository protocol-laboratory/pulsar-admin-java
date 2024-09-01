package io.github.protocol.pulsar.admin.jdk;

public enum ProducerAccessMode {
    Shared,
    Exclusive,
    ExclusiveWithFencing,
    WaitForExclusive;
}
