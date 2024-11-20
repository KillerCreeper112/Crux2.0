package killercreepr.crux.core.util;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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

    public static <T> T getRandom(@NotNull List<T> list){
        if(list.isEmpty()) return null;
        return list.get(CruxMath.random(0, list.size()-1));
    }
}
