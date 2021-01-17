package io.github.aluria.region.util.sql_reader;

import dev.king.universal.shared.api.batch.ComputedBatchQuery;
import dev.king.universal.shared.api.functional.SafetyBiConsumer;
import dev.king.universal.shared.api.functional.SafetyFunction;
import dev.king.universal.wrapper.mysql.MysqlProvider;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Supplier;

@Getter
public final class SQLReader {

    private final Map<String, SQLReaderEntity> sqlReaderEntities;
    private final ForkJoinPool forkJoinPool;
    private final MysqlProvider provider;
    private final Plugin plugin;


    public SQLReader(@NonNull Plugin plugin, @NonNull MysqlProvider provider) {
        this.plugin = plugin;
        this.provider = provider;
        this.forkJoinPool = new ForkJoinPool(2);
        this.sqlReaderEntities = new HashMap<>();
    }

    public String getQuery(@NonNull String path) {
        if (!path.contains(".")) {
            return getRootParent().getLazyQuery(path);
        }

        final String[] constraintPathKeys = path.split("\\.");
        return getQueryParent(
          constraintPathKeys[0]
        ).getLazyQuery(constraintPathKeys[1]);
    }

    public SQLReaderEntity getRootParent() {
        return getQueryParent("root");
    }

    private SQLReaderEntity getQueryParent(@NonNull String parent) {
        SQLReaderEntity sqlReaderEntity = sqlReaderEntities.get(parent);
        if (sqlReaderEntity != null) return sqlReaderEntity;

        sqlReaderEntity = new SQLReaderEntity(plugin, parent);
        sqlReaderEntities.put(parent, sqlReaderEntity);

        return sqlReaderEntity;
    }

    public void closeConnection() {
        forkJoinPool.shutdown();
        provider.closeConnection();
    }

    public boolean openConnection() {
        return provider.openConnection();
    }

    public void createAllTableSchemas(String... paths) {
        for (String path : paths) {
            update(path.concat(".create"));
        }
    }

    private <T> CompletableFuture<T> supplyAsync(@NonNull Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, forkJoinPool);
    }

    public <K> K query(@NonNull String path, @NonNull SafetyFunction<ResultSet, K> consumer, Object... objects) {
        return provider.query(getQuery(path), consumer, objects);
    }

    public <K> List<K> map(@NonNull String path, @NonNull SafetyFunction<ResultSet, K> function, Object... objects) {
        return provider.map(getQuery(path), function, objects);
    }

    public CompletableFuture<Integer> update(@NonNull String path, Object... objects) {
        return supplyAsync(() -> {
            return provider.update(getQuery(path), objects);
        });
    }

    public <T> CompletableFuture<int[]> batch(@NonNull String path, SafetyBiConsumer<T, ComputedBatchQuery> batchFunction, Collection<T> collection) {
        return supplyAsync(() -> {
            return provider.batch(getQuery(path), batchFunction, collection);
        });
    }
}
