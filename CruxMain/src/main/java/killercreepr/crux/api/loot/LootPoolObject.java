package killercreepr.crux.api.loot;

import killercreepr.crux.api.data.Holder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface LootPoolObject<T> extends LootObject<T> {
    @Nullable Holder<Collection<T>> getItems(@NotNull LootContext ctx);
}
