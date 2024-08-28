package io.github.protocol.pulsar.admin.jdk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class BacklogQuota {

    /**
     * backlog quota by size in byte.
     */
    private Long limitSize;

    /**
     * backlog quota by time in second.
     */
    private Integer limitTime;

    private RetentionPolicy policy;

}
