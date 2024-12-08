package io.github.protocol.pulsar.admin.jdk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TopicStats {
    @JsonIgnore
    private int count;

    public double msgRateIn;

    public double msgRateOut;

    public double msgThroughputIn;

    public double msgThroughputOut;

    public long bytesInCounter;

    public long msgInCounter;

    public long bytesOutCounter;

    public long msgOutCounter;

    public double averageMsgSize;

    public boolean msgChunkPublished;

    public long storageSize;

    public long backlogSize;

    public long earliestMsgPublishTimeInBacklogs;

    public long offloadedStorageSize;

    public long lastOffloadLedgerId;

    public long lastOffloadSuccessTimeStamp;

    public long lastOffloadFailureTimeStamp;

    private List<PublisherStats> publishers = new ArrayList();

    private Map<String, PublisherStats> publishersMap = new ConcurrentHashMap<>();

    public int waitingPublishers;

    public Map<String, SubscriptionStats> subscriptions = new HashMap();

    public Map<String, ReplicatorStats> replication = new TreeMap();

    public String deduplicationStatus;

    public Long topicEpoch;

    public int nonContiguousDeletedMessagesRanges;

    public int nonContiguousDeletedMessagesRangesSerializedSize;

    public int delayedMessageIndexSizeInBytes;

    public CompactionStats compaction = new CompactionStats();

}
