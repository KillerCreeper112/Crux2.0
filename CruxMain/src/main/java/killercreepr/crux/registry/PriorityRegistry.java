package killercreepr.crux.registry;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public interface PriorityRegistry<T> {
    @NotNull T register(int priority, @NotNull T object);

    boolean removeFromPriority(@NotNull Key key);

    PriorityRegistry<T> forEachSorted(Consumer<? super T> action);

    @NotNull
    List<T> buildSortedList();
}
