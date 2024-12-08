package io.github.protocol.pulsar.admin.jdk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PersistentOfflineTopicStats {

    private Long storageSize;

    private Long totalMessages;

    private Long messageBacklog;

    private String brokerName;

    private String topicName;

    private List<LedgerDetails> dataLedgerDetails;

    private Map<String, CursorDetails> cursorDetails;

    private Date statGeneratedAt;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    public static class CursorDetails {

        public Long cursorBacklog;

        public Long cursorLedgerId;

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    public static class LedgerDetails {

        public Long entries;

        public Long timestamp;

        public Long size;

        public Long ledgerId;

    }

}
