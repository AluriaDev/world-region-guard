package io.github.aluria.region.command.parser;

import co.aikar.commands.InvalidCommandArgument;
import io.github.aluria.region.entity.RegionObject;
import lombok.Data;
import lombok.NonNull;

import java.lang.reflect.Method;
import java.util.*;

@Data
@SuppressWarnings("all")
public class PropertyObject {

    private final static Map<Class, ParserFunctionType> PARSERS;

    static {
        PARSERS = new HashMap<>();
        PARSERS.put(Integer.class, (property, value) -> Integer.parseInt(value));
        PARSERS.put(String.class, (property, value) -> new String());
        PARSERS.put(Set.class, (property, value) -> new HashSet<>(Arrays.asList(value.split(" "))));
        PARSERS.put(Enum.class, (property, value) -> Enum.valueOf(
          (Class<Enum>) property.getType(),
          value.toUpperCase()
        ));
        PARSERS.put(Boolean.class, (property, value) ->
          value.equals("true")
            ? true : (value.equals("false") ? false : null)
        );
        /*PARSERS = ImmutableMap
          .<Class, ParserFunctionType>builder()
          .put(Integer.class, (property, value) -> Integer.parseInt(value))
          .put(Enum.class, (property, value) -> Enum.valueOf((Class<Enum>) property.getType(), value.toUpperCase()))
          .put(Set.class, (property, value) -> new HashSet<>(Arrays.asList(value.split(" "))))
          .put(Boolean.class, (property, value) -> (value.equals("true") ? true : (value.equals("false") ? false : null)))
          .put(String.class, (property, value) -> new String())
          .build();*/
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
        for (Map.Entry<Class, ParserFunctionType> entry : PARSERS.entrySet()) {
            if (!entry.getKey().isAssignableFrom(type)) continue;
            return entry.getValue().apply(this, rawValue);
        }
        return null;
    }
}
