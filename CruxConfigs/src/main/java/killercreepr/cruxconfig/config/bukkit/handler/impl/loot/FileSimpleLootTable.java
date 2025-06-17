package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.loot.LootPool;
import killercreepr.crux.api.loot.LootTable;
import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.loot.SimpleLootPool;
import killercreepr.crux.core.loot.SimpleLootPoolObject;
import killercreepr.crux.core.loot.SimpleLootTable;
import killercreepr.crux.core.util.CruxObjects;
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

public class FileSimpleLootTable<T> implements FileObjectHandler<LootTable<T>> {
    protected final @NotNull Class<T> type;
    protected final @NotNull FileSimpleLootPool<T> fileSimpleLootPool;
    protected final @Nullable KeyedRegistry<LootTable<T>> lootTableRegistry;
    public FileSimpleLootTable(@NotNull Class<T> type, @NotNull FileSimpleLootPool<T> fileSimpleLootPool, @Nullable KeyedRegistry<LootTable<T>> lootTableRegistry) {
        this.type = type;
        this.fileSimpleLootPool = fileSimpleLootPool;
        this.lootTableRegistry = lootTableRegistry;
    }

    public FileSimpleLootTable(@NotNull Class<T> type, @NotNull FileSimpleLootPool<T> fileSimpleLootPool) {
        this(type, fileSimpleLootPool, null);
    }

    public FileSimpleLootTable(@NotNull Class<T> type, @Nullable KeyedRegistry<LootTable<T>> lootTableRegistry){
        this(type, new FileSimpleLootPool<>(type), lootTableRegistry);
    }

    public FileSimpleLootTable(@NotNull Class<T> type){
        this(type, new FileSimpleLootPool<>(type), null);
    }

    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull LootTable<T> object) {
        throw new UnsupportedOperationException("unsupported");
    }

    public Class<?> parserClass(){
        return type;
    }

    @Override
    public @Nullable LootTable<T> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)){
            T object = (T) ctx.getRegistry().deserializeFromFile(parserClass(), e);
            if(object==null) return null;
            return new SimpleLootTable<>(Crux.key("null"), NumberProvider.constant(1),
                List.of(
                    new SimpleLootPool<>(NumberProvider.constant(1), List.of(
                        new SimpleLootPoolObject<>(1, 0f, Holder.direct(Set.of(object)))
                    ), false)
                ));
        }
        FileRegistry registry = ctx.getRegistry();
        if(o.has("reference") && lootTableRegistry != null){
            Key reference = registry.deserializeFromFile(Key.class, o.get("reference"));
            if(reference != null){
                return lootTableRegistry.get(reference);
            }
        }

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
        return createLootTable(key, rolls, pools);
    }

    public @Nullable SimpleLootTable<T> createLootTable(@NotNull Key key, @NotNull NumberProvider rolls, @NotNull List<LootPool<T>> pools){
        return new SimpleLootTable<>(key, rolls, pools);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "simple_loot_table";
    }
}
