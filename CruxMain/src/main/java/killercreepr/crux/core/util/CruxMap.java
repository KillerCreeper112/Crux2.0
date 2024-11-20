package killercreepr.crux.core.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class CruxMap {
    public static <T> @NotNull List<Map.Entry<T, Float>> sortKeysByFloat(@Nullable Map<T, Float> temp) {
        if(temp == null || temp.isEmpty()) return new ArrayList<>();

        final Comparator<Map.Entry<T, Float>> COMPARATOR = Map.Entry.comparingByValue();
        Set<Map.Entry<T, Float>> entryOfMap = temp.entrySet();

        List<Map.Entry<T, Float>> entries = new ArrayList<>(entryOfMap);
        entries.sort(COMPARATOR);
        return entries;
    }

    public static <T> @NotNull List<T> sortMapByFloat(@Nullable Map<T, Float> temp, boolean farthest) {
        if(temp ==  null || temp.isEmpty()) return new ArrayList<>();

        List<Map.Entry<T, Float>> entries = new ArrayList<>(temp.entrySet());
        entries.sort(Map.Entry.comparingByValue());
        List<T> list = entries.stream()
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        if(farthest) Collections.reverse(list);

        return list;
    }

    public static <K, V> @NotNull Map<V, K> reverse(@NotNull Map<K, V> map){
        return reverse(map, new HashMap<>());
    }

    public static <K, V> @NotNull Map<V, K> reverse(@NotNull Map<K, V> map, @NotNull Map<V, K> reversedMap) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            V value = entry.getValue();
            K key = entry.getKey();

            reversedMap.put(value, key);
        }

        return reversedMap;
    }


}
