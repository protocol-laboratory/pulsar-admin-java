package io.github.protocol.pulsar.admin.jdk;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class SubscriptionMessageId {

    private Integer batchIndex = -1;

    private Long entryId = -1L;

    private Long ledgerId = -1L;

    private Integer partitionIndex = -1;

    private Map<String, String> properties = null;

    public static SubscriptionMessageId earliest() {
        SubscriptionMessageId subscriptionMessageId = new SubscriptionMessageId();
        return subscriptionMessageId;
    }

    public static SubscriptionMessageId latest() {
        SubscriptionMessageId subscriptionMessageId = new SubscriptionMessageId();
        subscriptionMessageId.setEntryId(Long.MAX_VALUE);
        subscriptionMessageId.setLedgerId(Long.MAX_VALUE);
        subscriptionMessageId.setPartitionIndex(Integer.MAX_VALUE);
        return subscriptionMessageId;
    }
}
