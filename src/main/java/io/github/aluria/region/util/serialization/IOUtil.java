package io.github.aluria.region.util.serialization;

import lombok.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class IOUtil {

    public static String readInputStream(@NonNull InputStream inputStream) {
        final StringBuilder stringBuilder = new StringBuilder();
        final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return stringBuilder.toString();
    }
}