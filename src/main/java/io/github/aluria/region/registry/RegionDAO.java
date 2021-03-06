package io.github.aluria.region.registry;

import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.aluria.common.utils.LocationUtil;
import io.github.aluria.region.entity.RegionFlag;
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
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Getter
public abstract class RegionDAO {

    private final static Gson GSON;

    static {
        GSON = new GsonBuilder()
          .disableHtmlEscaping()
          .create();
    }

    private final SQLReader sqlReader;

    protected RegionDAO(@NonNull SQLReader sqlReader) {
        this.sqlReader = sqlReader;
    }

    /**
     * Save all the container world into the database
     *
     * @param regionObjects collection from container world
     */
    public CompletableFuture<Void> saveAll(@NonNull List<RegionObject> regionObjects) {
        if (regionObjects.isEmpty()) return null;
        return sqlReader.batch(
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
     * @return future operation
     */
    public CompletableFuture<Void> save(@NonNull RegionObject regionObject) {
        return sqlReader.update(
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
     * @param regionObject region instance, created using the {@link RegionValidator#validate(String, String, Location, Location)}
     * @return future operation
     */
    public CompletableFuture<Integer> delete(@NonNull RegionObject regionObject) {
        return sqlReader.update(
          "region.delete",
          regionObject.getId().toString()
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
            Collectors.groupingBy(RegionObject::getWorld)
          );
    }

    private RegionObject regionCompute(@NonNull ResultSet resultSet) throws SQLException {
        final String worldName = resultSet.getString("world_name");
        final Location locationStart = deserialize(worldName, resultSet.getString("location_start"));
        final Location locationEnd = deserialize(worldName, resultSet.getString("location_end"));
        final String rawFlag = resultSet.getString("flag");

        return RegionValidator
          .validate(
            UUID.fromString(resultSet.getString("id")),
            resultSet.getString("region_name"),
            locationStart,
            locationEnd,
            fromFlagJson(rawFlag)
          )
          .setPriority(resultSet.getInt("priority"))
          .setDisplayName(resultSet.getString("display_name"));
    }

    private Location deserialize(@NonNull String worldName, @NonNull String rawLocation) {
        return LocationUtil.deserialize(worldName, rawLocation);
    }

    private String toJsonFlag(@NonNull RegionObject regionObject) {
        return GSON.toJson(regionObject.getFlag());
    }

    private RegionFlag fromFlagJson(@NonNull String json) {
        return GSON.fromJson(json, RegionFlag.class);
    }

    private Object[] updateObjects(@NonNull RegionObject regionObject) {
        return new Object[]{
          regionObject.getRawLocationStart(),
          regionObject.getRawLocationEnd(),
          regionObject.getDisplayName(),
          regionObject.getPriority(),
          toJsonFlag(regionObject),
          regionObject.getId().toString()
        };
    }

    private Object[] insertObjects(@NonNull RegionObject regionObject) {
        return new Object[]{
          regionObject.getId().toString(),
          regionObject.getWorldName(),
          regionObject.getName(),
          regionObject.getRawLocationStart(),
          regionObject.getRawLocationEnd(),
          regionObject.getDisplayName(),
          regionObject.getPriority(),
          toJsonFlag(regionObject)
        };
    }
}
