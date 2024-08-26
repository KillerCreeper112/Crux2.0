package killercreepr.crux.util;

import com.google.gson.internal.LazilyParsedNumber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        if(o instanceof Number n){
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
        }
        if(type.isAssignableFrom(o.getClass())) return type.cast(o);
        return null;
    }

    public static Number parseNumber(Number n){
        if(n instanceof LazilyParsedNumber l){
            if(l.toString().contains(".")) return l.doubleValue();
            return l.intValue();
        }
        return n;
    }
}
