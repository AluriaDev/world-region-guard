package io.github.aluria.region.command.parser;

import com.google.common.collect.ImmutableMap;
import io.github.aluria.region.entity.RegionObject;
import lombok.Getter;
import lombok.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

@Getter
public class PropertyFactory {

    private static final Map<Class<?>, Class<?>> WRAPPER;

    static {
        WRAPPER = ImmutableMap
          .<Class<?>, Class<?>>builder()
          .put(Boolean.TYPE, Boolean.class)
          .put(Byte.TYPE, Byte.class)
          .put(Character.TYPE, Character.class)
          .put(Double.TYPE, Double.class)
          .put(Float.TYPE, Float.class)
          .put(Integer.TYPE, Integer.class)
          .put(Long.TYPE, Long.class)
          .put(Short.TYPE, Short.class)
          .put(Void.TYPE, Void.class)
          .build();
    }

    private final List<PropertyObject> propertyObjects;
    private final Set<Method> methodSet;
    private final Set<Field> fieldSet;
    public PropertyFactory() {
        this.propertyObjects = new ArrayList<>();
        this.methodSet = getRecursiveDeclaredMethods(RegionObject.class);
        this.fieldSet = getRecursiveDeclaredFields(RegionObject.class);
    }

    public static <T> Class<T> wrapperOf(Class<T> clazz) {
        return clazz.isPrimitive()
          ? (Class) WRAPPER.get(clazz)
          : clazz;
    }

    public PropertyObject getPropertyObject(@NonNull String identifier) {
        for (PropertyObject propertyObject : propertyObjects) {
            if (propertyObject.getIdentifier().equalsIgnoreCase(identifier)) {
                return propertyObject;
            }
        }
        return null;
    }

    private Method getMethodByName(@NonNull String name, Class<?>... types) {
        for (Method method : methodSet) {
            if (method.getName().toLowerCase().endsWith(name.toLowerCase())
              && Arrays.equals(method.getParameterTypes(), types)) {
                return method;
            }
        }
        return null;
    }

    public PropertyFactory instructApplication() {
        for (Field declaredField : fieldSet) {
            final PropertyParser propertyParser = declaredField.getAnnotation(PropertyParser.class);
            if (propertyParser == null) continue;

            final String value = propertyParser.value();
            final String fieldName = value.isEmpty()
              ? declaredField.getName()
              : value;

            final Class<?> type = declaredField.getType();
            final Method declaredMethod = getMethodByName(fieldName, type);
            if (declaredMethod == null) continue;

            propertyObjects.add(new PropertyObject(
              fieldName,
              type,
              wrapperOf(type),
              propertyParser,
              declaredMethod
            ));
        }
        return this;
    }

    private Set<Method> getRecursiveDeclaredMethods(@NonNull Class<?> type) {
        final Set<Method> methodSet = new HashSet<>();
        for (Class<?> clazz = type; clazz != null; clazz = clazz.getSuperclass()) {
            methodSet.addAll(Arrays.asList(clazz.getDeclaredMethods()));
        }
        return methodSet;
    }

    private Set<Field> getRecursiveDeclaredFields(@NonNull Class<?> type) {
        final Set<Field> fieldSet = new HashSet<>();
        for (Class<?> clazz = type; clazz != null; clazz = clazz.getSuperclass()) {
            fieldSet.addAll(Arrays.asList(clazz.getDeclaredFields()));
        }
        return fieldSet;
    }
}
