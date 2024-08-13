package killercreepr.crux.loot.api;

import killercreepr.crux.data.Holder;
import org.jetbrains.annotations.Nullable;

public interface LootPoolObject<T> extends LootObject<T> {
    @Nullable Holder<T> getItem();
}
