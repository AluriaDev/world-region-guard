package io.github.aluria.region.util.sql_reader;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
            queryContent = readInputStream(resourceStream);
            rootParent.put(path, queryContent);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return queryContent;
    }

    private String readInputStream(@NonNull InputStream inputStream) {
        final StringBuilder stringBuilder = new StringBuilder();

        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return stringBuilder.toString();
    }
}
