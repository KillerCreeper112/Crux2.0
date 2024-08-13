package killercreepr.crux.loot.api;

import killercreepr.crux.loot.api.functions.LootFunction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface FunctionedObject<T> {
    @NotNull
    List<LootFunction<T>> getFunctions();
}
