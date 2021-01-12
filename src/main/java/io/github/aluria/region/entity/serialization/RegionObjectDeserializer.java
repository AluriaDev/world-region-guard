package io.github.aluria.region.entity.serialization;

import com.google.common.collect.Multimap;
import io.github.aluria.region.entity.RegionObject;
import io.github.aluria.region.entity.RegionValidator;
import io.github.aluria.region.util.LocationUtil;
import io.github.aluria.region.util.sql.reader.SQLReader;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@RequiredArgsConstructor
public final class RegionObjectDeserializer {

    private final SQLReader sqlReader;

    public Map<World, List<RegionObject>> deserializer() {
        return sqlReader
          .map("region.select_all", this::regionCompute)
          .parallelStream()
          .collect(groupingBy(RegionObject::getWorldBase));
    }

    private RegionObject regionCompute(@NonNull ResultSet resultSet) throws SQLException {
        final String worldName = resultSet.getString("world_name");
        final String regionName = resultSet.getString("name");
        final String rawLocationStart = resultSet.getString("location_start");
        final String rawLocationEnd = resultSet.getString("location_end");
        final int priority = resultSet.getInt("priority");

        final Location locationStart = LocationUtil.deserialize(worldName, rawLocationStart);
        final Location locationEnd = LocationUtil.deserialize(worldName, rawLocationEnd);

        return RegionValidator
          .validate(regionName, locationStart, locationEnd)
          .setPriority(priority);
    }

    public void loadAllIntoRegistry(@NonNull Multimap<World, RegionObject> registry) {
        for (Map.Entry<World, List<RegionObject>> entry : deserializer().entrySet()) {
            registry.putAll(entry.getKey(), entry.getValue());
        }
    }
}
