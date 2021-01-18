package io.github.aluria.region.registry;

import com.google.common.collect.Multimap;
import io.github.aluria.common.utils.LocationUtil;
import io.github.aluria.region.entity.RegionObject;
import io.github.aluria.region.entity.RegionValidator;
import io.github.aluria.region.util.sql_reader.SQLReader;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public abstract class RegionDAO {

    private final SQLReader sqlReader;

    protected RegionDAO(@NonNull SQLReader sqlReader) {
        this.sqlReader = sqlReader;
    }

    /**
     * Save all the container world into the database
     *
     * @param regionObjects collection from container world
     */
    public void saveAll(@NonNull List<RegionObject> regionObjects) {
        if (regionObjects.isEmpty()) return;
        sqlReader.batch(
          "region.update",
          (regionObject, batchQuery) -> batchQuery.compute(updateObjects(regionObject)),
          regionObjects
        ).thenAcceptAsync(results -> {
            final List<RegionObject> collect = fillQueryToInsert(regionObjects, results);
            if (collect == null || collect.isEmpty()) return;
            sqlReader.batch(
              "region.insert",
              (regionObject, batchQuery) -> batchQuery.compute(insertObjects(regionObject)),
              collect
            );
        });
    }

    private List<RegionObject> fillQueryToInsert(@NonNull List<RegionObject> regionObjects, int[] results) {
        if (results.length == 0) return null;
        final List<RegionObject> objects = new LinkedList<>();
        for (int index = 0; index < results.length; index++) {
            if (results[index] == 0) {
                objects.add(regionObjects.get(index));
            }
        }
        return objects;
    }

    /**
     * Save the region into the database
     *
     * @param regionObject region instance
     */
    public void save(@NonNull RegionObject regionObject) {
        sqlReader.update(
          "region.update",
          updateObjects(regionObject)
        ).thenAcceptAsync(status -> {
            if (status != 0) return;
            sqlReader.update(
              "region.insert",
              insertObjects(regionObject)
            );
        });
    }

    /**
     * Delete region from the database
     *
     * @param regionObject region instance, created using the {@link io.github.aluria.region.entity.RegionValidator#validate(String, Location, Location)}
     */
    public void delete(@NonNull RegionObject regionObject) {
        sqlReader.update(
          "region.delete",
          regionObject.getWorldBaseName(),
          regionObject.getName()
        );
    }

    /**
     * Pre load all objects from database to cache memory (pre populate)
     *
     * @param registry instance of registry from {@link RegionRegistry}
     */
    public void loadAllIntoRegistry(@NonNull Multimap<World, RegionObject> registry) {
        for (Map.Entry<World, List<RegionObject>> entry : deserializer().entrySet()) {
            registry.putAll(entry.getKey(), entry.getValue());
        }
    }

    private Map<World, List<RegionObject>> deserializer() {
        return sqlReader
          .map("region.select_all", this::regionCompute)
          .parallelStream()
          .collect(
            Collectors.groupingBy(RegionObject::getWorldBase)
          );
    }

    private RegionObject regionCompute(@NonNull ResultSet resultSet) throws SQLException {
        final String worldName = resultSet.getString("world_name");
        final String regionName = resultSet.getString("name");

        final Location locationStart = LocationUtil.deserialize(
          worldName, resultSet.getString("location_start")
        );

        final Location locationEnd = LocationUtil.deserialize(
          worldName, resultSet.getString("location_end")
        );

        return RegionValidator
          .validate(regionName, locationStart, locationEnd)
          .setPriority(resultSet.getInt("priority"))
          .setDisplayName(resultSet.getString("display_name"));
    }

    private Object[] updateObjects(@NonNull RegionObject regionObject) {
        return new Object[]{
          regionObject.getRawLocationStart(),
          regionObject.getRawLocationEnd(),
          regionObject.getDisplayName(),
          regionObject.getPriority(),
          regionObject.getWorldBaseName(),
          regionObject.getName()
        };
    }

    private Object[] insertObjects(@NonNull RegionObject regionObject) {
        return new Object[]{
          regionObject.getWorldBaseName(),
          regionObject.getName(),
          regionObject.getRawLocationStart(),
          regionObject.getRawLocationEnd(),
          regionObject.getDisplayName(),
          regionObject.getPriority()
        };
    }
}
