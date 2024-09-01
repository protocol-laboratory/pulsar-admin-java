package io.github.protocol.pulsar.admin.jdk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ConsumerStats {
    public double msgRateOut;

    public double msgThroughputOut;

    public long bytesOutCounter;

    public long msgOutCounter;

    public double msgRateRedeLiver;

    public double messageAckRate;

    public int chunkedMessageRate;

    public String consumerName;

    public int availablePermits;

    public long unackedMessages;

    public int avgMessagesPerEntry;

    public boolean blockedConsumerOnUnackedMsgs;

    public String readPositionWhenJoining;

    @JsonIgnore
    private int addressOffset = -1;

    @JsonIgnore
    private int addressLength;

    @JsonIgnore
    private int connectedSinceOffset = -1;

    @JsonIgnore
    private int connectedSinceLength;

    @JsonIgnore
    private int clientVersionOffset = -1;

    @JsonIgnore
    private int clientVersionLength;

    public long lastAckedTimestamp;

    public long lastConsumedTimestamp;

    public long lastConsumedFlowTimestamp;

    public List<String> keyHashRanges;

    public Map<String, String> metadata;
}
