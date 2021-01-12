package io.github.aluria.region.util.sql.reader;

import io.github.aluria.region.util.IOUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public final class SQLReaderEntity {

    private final Plugin plugin;
    private final String parent;

    private final Map<String, String> rootParent = new HashMap<>();

    public String getLazyQuery(@NonNull String path) {
        String queryContent = rootParent.get(path);
        if (queryContent != null) return queryContent;

        if (!path.endsWith(".sql")) path += ".sql";
        final String rawParentPath = String.format("sql/%s/%s", parent, path);

        try (InputStream resourceStream = plugin.getResource(rawParentPath)) {
            queryContent = IOUtil.readInputStream(resourceStream);
            rootParent.put(path, queryContent);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return queryContent;
    }
}
