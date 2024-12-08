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
public class CompactionStats {
    public long lastCompactionRemovedEventCount;

    public long lastCompactionSucceedTimestamp;

    public long lastCompactionFailedTimestamp;

    public long lastCompactionDurationTimeInMills;
}
