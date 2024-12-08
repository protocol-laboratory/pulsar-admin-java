package io.github.protocol.pulsar.admin.jdk;

public enum RetentionPolicy {

    /**
     * Policy which holds producer's send request until the resource becomes available (or holding times out).
     */
    producer_request_hold,

    /**
     * Policy which throws javax.jms.ResourceAllocationException to the producer.
     */
    producer_exception,

    /**
     * Policy which evicts the oldest message from the slowest consumer's backlog.
     */
    consumer_backlog_eviction,

}
