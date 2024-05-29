package killercreepr.crux.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CruxMap {
    public static <T> @NotNull List<Map.Entry<T, Float>> sortMapByFloat(@Nullable HashMap<T, Float> temp) {
        if(temp == null || temp.isEmpty()) return new ArrayList<>();

        final Comparator<Map.Entry<T, Float>> COMPARATOR = Map.Entry.comparingByValue();
        Set<Map.Entry<T, Float>> entryOfMap = temp.entrySet();

        List<Map.Entry<T, Float>> entries = new ArrayList<>(entryOfMap);
        entries.sort(COMPARATOR);
        return entries;
    }
}
