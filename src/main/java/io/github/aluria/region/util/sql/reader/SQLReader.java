package io.github.aluria.region.util.sql.reader;

import dev.king.universal.shared.api.JdbcProvider;
import dev.king.universal.shared.api.functional.SafetyFunction;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zkingboos, SaiintBrisson
 */
@Getter
@RequiredArgsConstructor
public final class SQLReader {

    @NonNull
    private final Plugin plugin;

    @NonNull
    private final JdbcProvider provider;

    private final Map<String, SQLReaderEntity> sqlReaderEntities = new HashMap<>();

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

    public void update(@NonNull String path, Object... objects) {
        provider.update(getQuery(path), objects);
    }

    public <K> K query(@NonNull String path, @NonNull SafetyFunction<ResultSet, K> consumer, Object... objects) {
        return provider.query(getQuery(path), consumer, objects);
    }

    public <K> List<K> map(@NonNull String path, @NonNull SafetyFunction<ResultSet, K> function, Object... objects) {
        return provider.map(getQuery(path), function, objects);
    }
}
