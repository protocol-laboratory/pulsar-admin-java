package io.github.protocol.pulsar.admin.jdk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MessageIdImpl {

    private Long ledgerId;

    private Integer entryId;

    private Integer partitionIndex;

    private Integer batchIndex;

    @Override
    public String toString() {
        if (batchIndex != null) {
            return String.valueOf(ledgerId) + ':' + entryId + ':' + partitionIndex + ':' + batchIndex;
        }
        return String.valueOf(ledgerId) + ':' + entryId + ':' + partitionIndex;
    }

}
