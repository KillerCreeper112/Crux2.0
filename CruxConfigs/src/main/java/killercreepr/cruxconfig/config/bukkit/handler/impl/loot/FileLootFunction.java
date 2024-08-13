package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import killercreepr.crux.loot.api.functions.LootFunction;
import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.SimpleMappedRegistry;
import killercreepr.cruxconfig.config.bukkit.handler.FileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileLootFunction implements FileHandler<LootFunction<?>> {
    public final MappedRegistry<String, CustomFileLootFunction<?>> CUSTOM_HANDLERS = new SimpleMappedRegistry<>();
    public void registerCustomHandler(@NotNull CustomFileLootFunction<?> handler){
        CUSTOM_HANDLERS.register(handler.getType(), handler);
    }
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull LootFunction<?> object) {
        throw new RuntimeException("unsupported");
    }

    @Override
    public @Nullable LootFunction<?> deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        String target = o.getObject(String.class, "target");
        return deserializeFromFile(context, e, target);
    }

    public @Nullable LootFunction<?> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e, @Nullable String target) {
        if(!(e instanceof FileObject o)) return null;
        String type = o.getObject(String.class, "type");
        if(type==null) return null;
        type = type.toLowerCase();
        CustomFileLootFunction<?> handler = CUSTOM_HANDLERS.get(type);
        if(handler==null) throw new IllegalStateException("LootFunction type " + type + " does not exist!");
        return handler.deserializeFromFile(ctx, o, target == null ? "this" : target);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "loot_function";
    }
}
