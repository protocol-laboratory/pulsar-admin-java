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
public class RetentionPolicies {

    private Integer retentionTimeInMinutes;

    private Integer retentionSizeInMB;

}
