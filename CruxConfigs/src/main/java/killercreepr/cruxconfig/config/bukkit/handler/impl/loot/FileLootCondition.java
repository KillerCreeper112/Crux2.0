package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import killercreepr.crux.loot.conditions.LootCondition;
import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.SimpleMappedRegistry;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileLootCondition implements FileObjectHandler<LootCondition> {
    public final MappedRegistry<String, CustomFileLootCondition<?>> CUSTOM_HANDLERS = new SimpleMappedRegistry<>();
    public void registerCustomHandler(@NotNull CustomFileLootCondition<?> handler){
        CUSTOM_HANDLERS.register(handler.getType(), handler);
    }
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull LootCondition object) {
        throw new RuntimeException("unsupported");
    }

    @Override
    public @Nullable LootCondition deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        String target = o.getObject(String.class, "target");
        return deserializeFromFile(context, e, target);
    }

    public @Nullable LootCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e, @Nullable String target) {
        if(!(e instanceof FileObject o)) return null;
        String type = o.getObject(String.class, "condition");
        if(type==null) return null;
        type = type.toLowerCase();
        CustomFileLootCondition<?> handler = CUSTOM_HANDLERS.get(type);
        if(handler==null) throw new IllegalStateException("LootCondition type " + type + " does not exist!");
        return handler.deserializeFromFile(ctx, o, target == null ? "this" : target);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "loot_condition";
    }
}
