package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import killercreepr.crux.api.loot.item.ItemLootPoolObject;
import killercreepr.crux.api.registry.MappedRegistry;
import killercreepr.crux.core.registry.SimpleMappedRegistry;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileItemLootPoolObject implements FileObjectHandler<ItemLootPoolObject> {
    public final MappedRegistry<String, CustomFilePoolObject<?>> CUSTOM_HANDLERS = new SimpleMappedRegistry<>();
    public void registerCustomHandler(@NotNull CustomFilePoolObject<?> handler){
        CUSTOM_HANDLERS.register(handler.getType(), handler);
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
        String type = o.getObject(String.class, "type");
        if(type==null) return null;
        type = type.toLowerCase();
        CustomFilePoolObject<?> handler = CUSTOM_HANDLERS.get(type);
        if(handler==null) throw new IllegalStateException("ItemLootPoolObject type " + type + " does not exist!");
        return handler.deserializeFromFile(ctx, o);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "item_loot_pool_object";
    }
}
