package io.github.aluria.region.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public final class RegionFlagObject {

    private boolean canBreak = true;
}
