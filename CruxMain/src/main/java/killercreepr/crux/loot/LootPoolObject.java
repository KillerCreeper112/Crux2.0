package killercreepr.crux.loot;

import killercreepr.crux.data.Holder;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface LootPoolObject<T> extends LootObject<T> {
    @Nullable Holder<Collection<T>> getItems();
}
