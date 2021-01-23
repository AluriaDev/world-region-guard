package io.github.aluria.region.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public abstract class RegionFlagObject {

    private boolean canBreak = true;
    private boolean canPlace = true;
    private boolean canExecuteCommand = true;
    private boolean canChat = true;
}
