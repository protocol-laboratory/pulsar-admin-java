package io.github.protocol.pulsar.admin.jdk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PublisherStats {
    @JsonIgnore
    private int count;

    public ProducerAccessMode accessMode;

    public double msgRateIn;

    public double msgThroughputIn;

    public double averageMsgSize;

    public double chunkedMessageRate;

    public long producerId;

    public boolean supportsPartialProducer;

    @JsonIgnore
    private int producerNameOffset = -1;

    @JsonIgnore
    private int producerNameLength;

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

    @JsonIgnore
    private StringBuilder stringBuilder = new StringBuilder();

    public Map<String, String> metadata;
}
