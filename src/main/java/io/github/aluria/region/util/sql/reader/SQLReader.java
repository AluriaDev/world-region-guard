package io.github.aluria.region.util.sql.reader;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

/**
 * @author SaiintBrisson (c)
 */
@Getter
@RequiredArgsConstructor
public final class SQLReader {

    @NonNull
    private final Plugin plugin;

    private final HashMap<String, SQLReaderEntity> sqlReaderEntities = new HashMap<>();

    public String getQuery(@NonNull String path) {
        if (!path.contains(".")) {
            return getRootParent().getLazyQuery(path);
        }

        final String[] constraintPathKeys = path.split("\\.");
        return getQueryParent(constraintPathKeys[0])
          .getLazyQuery(constraintPathKeys[1]);
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
}
