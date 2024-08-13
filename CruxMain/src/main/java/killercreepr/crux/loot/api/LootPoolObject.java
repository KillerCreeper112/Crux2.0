package killercreepr.crux.loot.api;

import killercreepr.crux.data.Holder;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface LootPoolObject<T> extends LootObject<T> {
    @Nullable Holder<Collection<T>> getItems();
}
