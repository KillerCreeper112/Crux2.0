package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import killercreepr.crux.api.loot.item.ItemLootFunction;
import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileItemLootFunction implements FileObjectHandler<ItemLootFunction> {
    public final KeyedRegistry<CustomFileItemLootFunction<?>> CUSTOM_HANDLERS = KeyedRegistry.keyedRegistry();
    public void registerCustomHandler(@NotNull CustomFileItemLootFunction<?> handler){
        CUSTOM_HANDLERS.register(handler);
    }
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull ItemLootFunction object) {
        throw new RuntimeException("unsupported");
    }

    @Override
    public @Nullable ItemLootFunction deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        String target = o.getObject(String.class, "target");
        return deserializeFromFile(context, e, target);
    }

    public @Nullable ItemLootFunction deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e, @Nullable String target) {
        if(!(e instanceof FileObject o)) return null;
        Key type = o.getObject(Key.class, "function");
        if(type==null) return null;
        CustomFileItemLootFunction<?> handler = CUSTOM_HANDLERS.get(type);
        if(handler==null) throw new IllegalStateException("LootFunction type " + type + " does not exist!");
        return handler.deserializeFromFile(ctx, o, target == null ? "this" : target);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "item_loot_function";
    }
}
