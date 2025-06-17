package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.loot.LootPoolObject;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.loot.SimpleLootPool;
import killercreepr.crux.core.loot.SimpleLootPoolObject;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class FileSimpleLootPool<T> implements FileObjectHandler<SimpleLootPool<T>> {
    protected final @NotNull Class<T> type;
    public FileSimpleLootPool(@NotNull Class<T> type) {
        this.type = type;
    }

    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull SimpleLootPool<T> object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable SimpleLootPool<T> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)){
            if(e instanceof FileGeneric g){
                T object = ctx.getRegistry().deserializeFromFile(type, g);
                if(object==null) return null;
                Collection<T> objects = Set.of(object);
                return new SimpleLootPool<>(NumberProvider.constant(1), List.of(
                    new SimpleLootPoolObject<>(1, 0f, Holder.direct(objects))
                ), false);
            }
            return null;
        }
        FileRegistry registry = ctx.getRegistry();
        NumberProvider rolls = registry.deserializeFromFile(NumberProvider.class, o.get("rolls"));
        if(rolls == null) rolls = NumberProvider.constant(1);

        List<LootCondition> conditions = registry.deserializeFromFile(
            new TypeToken<List<LootCondition>>(){}.getType(), o.get("conditions")
        );
        List<LootPoolObject<T>> entries = new ArrayList<>(); /*registry.deserializeFromFile(
            new TypeToken<List<SimpleLootPoolObject<T>>>(){}.getType(), o.get("entries")
        );*/
        if(!(o.get("entries") instanceof FileArray a)) return null;
        FileSimpleLootPoolObject<T> lootPoolObjectHandler = new FileSimpleLootPoolObject<>(type);
        a.forEach(ele ->{
            SimpleLootPoolObject<T> object = lootPoolObjectHandler.deserializeFromFile(ctx, ele);
            if(object==null) return;
            entries.add(object);
        });
        if(entries.isEmpty()) return null;

        boolean allowDuplicates = o.getOrDefaultObject(Boolean.class, "allow_duplicates",false);
        return new SimpleLootPool<>(
            conditions, null, rolls, entries, allowDuplicates
        );
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "simple_loot_pool";
    }
}
