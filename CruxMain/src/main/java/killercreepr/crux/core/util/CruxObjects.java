package killercreepr.crux.core.util;

import com.google.gson.internal.LazilyParsedNumber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public class CruxObjects {
    public static boolean checkNull(Object... objects){
        for(Object o : objects){
            if(o==null) return true;
        }
        return false;
    }

    public static boolean checkAllNull(Object... objects){
        int amount = 0;
        for(Object o : objects){
            if(o==null) amount++;
        }
        return amount == objects.length;
    }

    public static void requireNotNull(Object... objects){
        for(Object o : objects){
            if(o==null) throw new NullPointerException();
        }
    }

    public static void requireNotNull(String msg, Object... objects){
        for(Object o : objects){
            if(o==null) throw new NullPointerException(msg);
        }
    }

    public static <T> @NotNull T castOrThrow(@NotNull Class<T> type, @NotNull Object o){
        T casted = attemptCast(type, o);
        if(casted==null) throw new ClassCastException("Object cannot be cast to " + type.getSimpleName() + " (" + o + ")! " + o.getClass().getSimpleName());
        return casted;
    }

    public static <T> @Nullable T attemptCast(@NotNull Class<T> type, @NotNull Object o){
        o = unboxIfNecessary(type, o, null);

        if (type.isPrimitive()) {
            if (type == boolean.class && o instanceof Boolean b) return (T) (Boolean) b;
            if (type == byte.class && o instanceof Number n) return (T) (Byte) n.byteValue();
            if (type == short.class && o instanceof Number n) return (T) (Short) n.shortValue();
            if (type == int.class && o instanceof Number n) return (T) (Integer) n.intValue();
            if (type == long.class && o instanceof Number n) return (T) (Long) n.longValue();
            if (type == float.class && o instanceof Number n) return (T) (Float) n.floatValue();
            if (type == double.class && o instanceof Number n) return (T) (Double) n.doubleValue();
            if (type == char.class && o instanceof Character c) return (T) (Character) c;
        }

        /*if(o instanceof Number n){
            if (type == short.class || type == Short.class) {
                return (T) Short.class.cast(n.shortValue());
            } else if (type == int.class || type == Integer.class) {
                return (T) Integer.class.cast(n.intValue());
            } else if (type == long.class || type == Long.class) {
                return (T) Long.class.cast(n.longValue());
            } else if (type == float.class || type == Float.class) {
                return (T) Float.class.cast(n.floatValue());
            } else if (type == double.class || type == Double.class) {
                return (T) Double.class.cast(n.doubleValue());
            }
        }*/
        if(type.isAssignableFrom(o.getClass())) return type.cast(o);
        return null;
    }

    public static Object unboxIfNecessary(Type type, Object value, Object fallback){
        if (value == null) return fallback;
        if (type instanceof Class<?> clazz) {
            if(Number.class.isAssignableFrom(clazz) && !(value instanceof Number)){
                try{
                    value = Double.parseDouble(value.toString());
                } catch (IllegalArgumentException e) {
                }
            }

            if (double.class.isAssignableFrom(clazz) || Double.class.isAssignableFrom(clazz)) {
                return ((Number) value).doubleValue();
            }
            if (clazz == float.class || Float.class.isAssignableFrom(clazz)) {
                return ((Number) value).floatValue();
            }
            if (clazz == short.class || Short.class.isAssignableFrom(clazz)) {
                return ((Number) value).shortValue();
            }
            if (clazz == long.class || Long.class.isAssignableFrom(clazz)) {
                return ((Number) value).longValue();
            }
            if (clazz == int.class || Integer.class.isAssignableFrom(clazz)) {
                return ((Number) value).intValue();
            }
            if (clazz == byte.class || Byte.class.isAssignableFrom(clazz)) {
                return ((Number) value).byteValue();
            }
            if (clazz == char.class || Character.class.isAssignableFrom(clazz)) {
                return (value instanceof Character)
                    ? (Character) value
                    : ((String) value).charAt(0);
            }
            if (clazz == boolean.class || Boolean.class.isAssignableFrom(clazz)) {
                if(value instanceof String || value instanceof Number){
                    return CruxString.parseBoolean(value.toString());
                }
                return ((Boolean) value).booleanValue();
            }
        }
        return value;
    }

    public static Number parseNumber(Number n){
        if(n instanceof LazilyParsedNumber l){
            if(l.toString().contains(".")) return l.doubleValue();
            return l.intValue();
        }
        return n;
    }
}
