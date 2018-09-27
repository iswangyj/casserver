package org.jasig.cas.ticket.registry.support.kryo;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author SxL
 * Created on 9/25/2018 4:57 PM.
 */
public final class FieldHelper {
    private final Map<String, Field> fieldCache = new HashMap();

    public FieldHelper() {
    }

    public Object getFieldValue(Object target, String fieldName) {
        Field f = this.getField(target, fieldName);

        try {
            return f.get(target);
        } catch (IllegalAccessException var5) {
            throw new IllegalStateException("Error getting field value", var5);
        }
    }

    public void setFieldValue(Object target, String fieldName, Object value) {
        Field f = this.getField(target, fieldName);

        try {
            f.set(target, value);
        } catch (IllegalAccessException var6) {
            throw new IllegalStateException("Error setting field value", var6);
        }
    }

    private Field getField(Object target, String name) {
        Class<?> clazz = target.getClass();
        String key = clazz.getName() + '.' + name;
        Field f = (Field)this.fieldCache.get(key);

        while(f == null) {
            try {
                f = clazz.getDeclaredField(name);
                f.setAccessible(true);
                this.fieldCache.put(key, f);
            } catch (NoSuchFieldException var7) {
                clazz = clazz.getSuperclass();
                if (clazz == null) {
                    throw new IllegalStateException("No such field " + key);
                }
            }
        }

        return f;
    }
}

