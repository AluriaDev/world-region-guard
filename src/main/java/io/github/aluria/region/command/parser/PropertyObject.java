package io.github.aluria.region.command.parser;

import co.aikar.commands.InvalidCommandArgument;
import com.google.common.collect.ImmutableMap;
import io.github.aluria.region.entity.RegionObject;
import lombok.Data;
import lombok.NonNull;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

@Data
@SuppressWarnings("all")
public class PropertyObject {

    private final static Map<Class<?>, BiFunction<PropertyObject, String, Object>> PARSERS;

    static {
        PARSERS = ImmutableMap
          .<Class<?>, BiFunction<PropertyObject, String, Object>>builder()
          .put(Integer.class, (property, value) -> Integer.parseInt(value))
          .put(Enum.class, (property, value) -> Enum.valueOf((Class<Enum>) property.getType(), value.toUpperCase()))
          .put(Set.class, (property, value) -> new HashSet<>(Arrays.asList(value.split(" "))))
          .put(Boolean.class, (property, value) -> value.equals("true") ? true : (value.equals("false") ? false : null))
          .put(String.class, (property, value) -> value)
          .build();
    }

    private final String identifier;
    private final Class<?> declaredType, type;
    private final PropertyParser parser;
    private final Method method;

    public void invokeMethod(@NonNull RegionObject regionObject, @NonNull String rawValue) {
        try {
            final Object value = parseRawValue(rawValue);
            method.invoke(regionObject, value);
        } catch (Exception exception) {
            throw new InvalidCommandArgument(exception.getMessage());
        }
    }

    private Object parseRawValue(@NonNull String rawValue) {
        for (Map.Entry<Class<?>, BiFunction<PropertyObject, String, Object>> entry : PARSERS.entrySet()) {
            if (!entry.getKey().isAssignableFrom(type)) continue;
            return entry.getValue().apply(this, rawValue);
        }
        return null;
    }
}
