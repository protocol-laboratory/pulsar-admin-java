package io.github.protocol.pulsar.admin.jdk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReplicatorStats {
    public double msgRateIn;

    public double msgThroughputIn;

    public double msgRateOut;

    public double msgThroughputOut;

    public double msgRateExpired;

    public long replicationBacklog;

    public boolean connected;

    public long replicationDelayInSeconds;

    public String inboundConnection;

    public String inboundConnectedSince;

    public String outboundConnection;

    public String outboundConnectedSince;
}
