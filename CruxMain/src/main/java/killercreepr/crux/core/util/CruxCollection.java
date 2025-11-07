package killercreepr.crux.core.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class CruxCollection {
    public static <T> T getRandom(@NotNull Collection<T> set) {
        if(set.isEmpty()) return null;
        if(set instanceof List<T> d) return getRandom(d);
        int index = CruxMath.random().nextInt(set.size());

        Iterator<T> iterator = set.iterator();
        T randomElement = null;

        for (int i = 0; i <= index; i++) {
            randomElement = iterator.next();
        }

        return randomElement;
    }

    public static <T> void shuffle(List<T> list, Random random) {
        int size = list.size();

        for(int i = size; i > 1; --i) {
            int randomInt = random.nextInt(i);
            list.set(i - 1, list.set(randomInt, list.get(i - 1)));
        }

    }

    public static <T> T getRandom(@NotNull List<T> list){
        if(list.isEmpty()) return null;
        return list.get(CruxMath.random(0, list.size()-1));
    }

    @SafeVarargs
    public static <T> T getRandom(@NotNull T... list){
        if(list.length < 1) return null;
        return list[(CruxMath.random(0, list.length-1))];
    }

    public static <T> T getFirst(@Nullable Collection<T> list){
        if(list == null) return null;
        for(T t : list){
            return t;
        }
        return null;
    }

    @SafeVarargs
    public static <T> T getFirst(@NotNull T... list){
        if(list == null || list.length < 1) return null;
        return list[0];
    }

    public static <T> T getLast(@Nullable List<T> list){
        if (list == null || list.isEmpty()) return null;
        return list.getLast();
    }

    public static <T> T getLast(@Nullable Collection<T> list){
        if (list == null || list.isEmpty()) return null;

        if (list instanceof List<T> l) {
            return getLast(l);
        }

        T last = null;
        for (T item : list) {
            last = item;
        }
        return last;
    }

    @SafeVarargs
    public static <T> T getLast(@NotNull T... list){
        if(list.length < 1) return null;
        return list[list.length-1];
    }

    public static <T> boolean testAny(@NotNull Collection<T> collection, @NotNull Predicate<T> test){
        for(T t : collection){
            if(test.test(t)) return true;
        }
        return false;
    }

    public static <T> boolean testAll(@NotNull Collection<T> collection, @NotNull Predicate<T> test){
        for(T t : collection){
            if(!test.test(t)) return false;
        }
        return true;
    }
}
