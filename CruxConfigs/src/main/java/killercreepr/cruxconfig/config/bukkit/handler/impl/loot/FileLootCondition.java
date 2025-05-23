package killercreepr.cruxconfig.config.bukkit.handler.impl.loot;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.crux.core.loot.conditions.AllOfCondition;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class FileLootCondition implements FileObjectHandler<LootCondition> {
    public final KeyedRegistry<CustomFileLootCondition<?>> CUSTOM_HANDLERS = KeyedRegistry.keyedRegistry();
    public void registerCustomHandler(@NotNull CustomFileLootCondition<?> handler){
        CUSTOM_HANDLERS.register(handler);
    }
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull LootCondition object) {
        throw new RuntimeException("unsupported");
    }

    @Override
    public @Nullable LootCondition deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        String target = e instanceof FileObject o ? o.getObject(String.class, "target") : null;
        return deserializeFromFile(context, e, target);
    }

    public @Nullable LootCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e, @Nullable String target) {
        if(!(e instanceof FileObject o)){
            if(e instanceof FileArray){
                Collection<LootCondition> conditions = ctx.getRegistry().deserializeFromFile(
                    new TypeToken<Collection<LootCondition>>(){}.getType(), e
                );
                if(conditions == null) return null;
                return new AllOfCondition(conditions);
            }
            return null;
        }
        FileRegistry reg = ctx.getRegistry();
        Key type = reg.deserializeFromFile(Key.class, o.get("condition"));
        if(type==null) return null;
        CustomFileLootCondition<?> handler = CUSTOM_HANDLERS.get(type);
        if(handler==null) throw new IllegalStateException("LootCondition type " + type + " does not exist!");
        return handler.deserializeFromFile(ctx, o, target == null ? "this" : target);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "loot_condition";
    }
}
