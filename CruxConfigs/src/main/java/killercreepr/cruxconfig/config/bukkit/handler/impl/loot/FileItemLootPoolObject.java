package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import killercreepr.crux.api.loot.item.ItemLootPoolObject;
import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileItemLootPoolObject implements FileObjectHandler<ItemLootPoolObject> {
    public final KeyedRegistry<CustomFileItemPoolObject<?>> CUSTOM_HANDLERS = KeyedRegistry.keyedRegistry();
    public void registerCustomHandler(@NotNull CustomFileItemPoolObject<?> handler){
        CUSTOM_HANDLERS.register(handler);
    }
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull ItemLootPoolObject object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable ItemLootPoolObject deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        String target = o.getObject(String.class, "target");
        return deserializeFromFile(context, e, target);
    }

    public @Nullable ItemLootPoolObject deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e, @Nullable String target) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry reg = ctx.getRegistry();
        Key type = reg.deserializeFromFile(Key.class, o.get("type"));
        if(type==null) return null;
        CustomFileItemPoolObject<?> handler = CUSTOM_HANDLERS.get(type);
        if(handler==null) throw new IllegalStateException("ItemLootPoolObject type " + type + " does not exist!");
        return handler.deserializeFromFile(ctx, o);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "item_loot_pool_object";
    }
}
