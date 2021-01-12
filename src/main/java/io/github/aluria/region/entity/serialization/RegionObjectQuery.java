package io.github.aluria.region.entity.serialization;

import io.github.aluria.region.entity.RegionObject;
import io.github.aluria.region.util.sql.reader.SQLReader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class RegionObjectQuery {

    private final SQLReader reader;

    public void deleteRegionFromDatabase(@NonNull RegionObject regionObject) {
        reader.update(
          "region.delete",
          regionObject.getWorldBase().getName(),
          regionObject.getName()
        );
    }
}
