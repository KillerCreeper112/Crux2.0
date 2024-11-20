package killercreepr.crux.api.loot.functions;

import killercreepr.crux.api.loot.LootContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LootFunction<T> {
    T accept(@Nullable T i, @NotNull LootContext context);
    boolean test(@NotNull LootContext context);
}
