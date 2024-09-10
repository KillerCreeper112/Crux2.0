package killercreepr.crux.loot.opened;

import killercreepr.crux.loot.LootPoolObject;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface OpenedLootObject<T> {
    @NotNull
    Collection<LootPoolObject<T>> getAllItems();
}
