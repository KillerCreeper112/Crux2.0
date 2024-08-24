package killercreepr.crux.loot;

import killercreepr.crux.loot.functions.LootFunction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface FunctionedObject<T> {
    @NotNull
    List<LootFunction<T>> getFunctions();
}
