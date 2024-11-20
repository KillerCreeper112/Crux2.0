package killercreepr.crux.api.loot;

import killercreepr.crux.api.loot.functions.LootFunction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface FunctionedObject<T> {
    @NotNull
    List<LootFunction<T>> getFunctions();
    default T applyFunctions(@NotNull T item, @NotNull LootContext ctx){
        for(LootFunction<T> function : getFunctions()){
            item = function.accept(item, ctx);
        }
        return item;
    }
}
