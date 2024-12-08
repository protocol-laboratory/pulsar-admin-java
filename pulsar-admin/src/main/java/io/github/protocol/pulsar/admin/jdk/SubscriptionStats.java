package io.github.protocol.pulsar.admin.jdk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubscriptionStats {
    public double msgRateOut;

    public double msgThroughputOut;

    public long bytesOutCounter;

    public long msgOutCounter;

    public double msgRateRedeLiver;

    public double messageAckRate;

    public int chunkedMessageRate;

    public long msgBacklog;

    public long backlogSize;

    public long earliestMsgPublishTimeInBacklog;

    public long msgBacklogNoDelayed;

    public boolean blockedSubscriptionOnUnackedMsgs;

    public boolean msgDelayed;

    public long unackedMessages;

    public String type;

    public String activeConsumerName;

    public double msgRateExpired;

    public long totalMsgExpired;

    public long lastExpireTimestamp;

    public long lastConsumedFlowTimestamp;

    public long lastConsumedTimestamp;

    public long lastAckedTimestamp;

    public long lastMarkDeleteAdvancedTimestamp;

    public List<ConsumerStats> consumers = new ArrayList();

    public boolean isDurable;

    public boolean isReplicated;

    public boolean allowOutOfOrderDelivery;

    public String keySharedMode;

    public Map<String, String> consumesAfterMarkDeletePosition = new LinkedHashMap();

    public int nonContiguousDeletedMessagesRanges;

    public int nonContiguousDeletedMessagesRangesSerializedSize;

    public long delayedTrackerMemoryUsage;

    public Map<String, String> subsriptionProperties = new HashMap<>();

    public long filterProcessedMsgCount;

    public long filterAcceptedMsgCount;

    public long filterRejectedMsgCount;

    public long filterRescheduledMsgCount;
}
