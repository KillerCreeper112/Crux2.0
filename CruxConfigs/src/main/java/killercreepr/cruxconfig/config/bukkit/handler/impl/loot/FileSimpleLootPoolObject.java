package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.core.loot.SimpleLootPoolObject;
import killercreepr.crux.core.loot.item.SimpleItemLootObject;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

public class FileSimpleLootPoolObject<T> implements FileObjectHandler<SimpleLootPoolObject<T>> {
    protected final @NotNull Class<T> type;
    protected final @NotNull Type collectionType;
    public FileSimpleLootPoolObject(@NotNull Class<T> type) {
        this.type = type;
        this.collectionType = new ParameterizedType(){

            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{type};
            }

            @Override
            public Type getRawType() {
                return Collection.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }

    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull SimpleLootPoolObject<T> object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable SimpleLootPoolObject<T> deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        String target = o.getObject(String.class, "target");
        return deserializeFromFile(context, e, target);
    }

    public @Nullable SimpleLootPoolObject<T> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e, @Nullable String target) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        SimpleItemLootObject loot = registry.deserializeFromFile(SimpleItemLootObject.class, e);
        if(loot==null) return null;

        Collection<T> itemKeys = registry.deserializeFromFile(
            collectionType, o.get("item")
        );
        /*if(o.get("item") instanceof FileGeneric){
            T k = registry.deserializeFromFile(type, o.get("item"));
            if(k==null) return null;
            itemKeys = new HashSet<>();
            itemKeys.add(k);
        }else itemKeys = registry.deserializeFromFile(
            new TypeToken<Collection<T>>(){}.getType(), o.get("item")
        );*/

        if(itemKeys == null || itemKeys.isEmpty()) return null;

        return new SimpleLootPoolObject<>(loot.getWeight(), loot.getQuality(), Holder.direct(itemKeys));
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "simple_loot_pool_object";
    }
}
