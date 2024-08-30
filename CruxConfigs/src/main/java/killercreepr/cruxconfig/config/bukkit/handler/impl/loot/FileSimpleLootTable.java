package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import killercreepr.crux.Crux;
import killercreepr.crux.data.Holder;
import killercreepr.crux.loot.LootPool;
import killercreepr.crux.loot.impl.SimpleLootPool;
import killercreepr.crux.loot.impl.SimpleLootPoolObject;
import killercreepr.crux.loot.impl.SimpleLootTable;
import killercreepr.crux.util.CruxObjects;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FileSimpleLootTable<T> implements FileObjectHandler<SimpleLootTable<T>> {
    protected final @NotNull Class<T> type;
    protected final @NotNull FileSimpleLootPool<T> fileSimpleLootPool;
    public FileSimpleLootTable(@NotNull Class<T> type, @NotNull FileSimpleLootPool<T> fileSimpleLootPool) {
        this.type = type;
        this.fileSimpleLootPool = fileSimpleLootPool;
    }

    public FileSimpleLootTable(@NotNull Class<T> type){
        this(type, new FileSimpleLootPool<>(type));
    }

    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull SimpleLootTable<T> object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable SimpleLootTable<T> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)){
            T object = ctx.getRegistry().deserializeFromFile(type, e);
            if(object==null) return null;
            return new SimpleLootTable<>(Crux.key("null"), NumberProvider.constant(1),
                List.of(
                    new SimpleLootPool<>(1, 0f, NumberProvider.constant(1), List.of(
                        new SimpleLootPoolObject<>(1, 0f, Holder.direct(Set.of(object)))
                    ))
                ));
        }
        FileRegistry registry = ctx.getRegistry();
        Key key = registry.deserializeFromFile(Key.class, o.get("key"));
        if(key==null) key = Crux.key("null");
        return deserializeFromFile(ctx, o, key);
    }

    public @Nullable SimpleLootTable<T> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull Key key) {
        FileRegistry registry = ctx.getRegistry();
        NumberProvider rolls = registry.deserializeFromFile(NumberProvider.class, e.get("rolls"));
        if(rolls == null) rolls = NumberProvider.constant(1);
        if(!(e.get("pools") instanceof FileArray a)) return null;
        List<LootPool<T>> pools = new ArrayList<>();
        a.forEach(ele ->{
            SimpleLootPool<T> pool = fileSimpleLootPool.deserializeFromFile(ctx, ele);
            if(pool==null) return;
            pools.add(pool);
        });
        if(CruxObjects.checkNull(pools)) return null;
        return new SimpleLootTable<>(
            key, rolls, pools
        );
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "simple_loot_table";
    }
}
