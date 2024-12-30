package killercreepr.cruxadvancements.core.config.handler;

import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.cruxadvancements.api.advancement.objective.AdvancementObjective;
import killercreepr.cruxadvancements.core.advancement.objective.ObjectiveCommonData;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileAdvancementObjective implements FileObjectHandler<AdvancementObjective> {
    public static final KeyedRegistry<CustomFileAdvancementObjective<?>> CUSTOM_HANDLERS = KeyedRegistry.keyedRegistry();
    public static void registerCustomHandler(@NotNull CustomFileAdvancementObjective<?> handler){
        CUSTOM_HANDLERS.register(handler);
    }
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull AdvancementObjective object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable AdvancementObjective deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        String type = o.getObject(String.class, "type");
        if(type==null) return null;
        String criterion = o.getObject(String.class, "criterion");
        if(criterion==null) return null;
        return deserializeFromFile(context, e, criterion);
    }

    public static @Nullable AdvancementObjective deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e, @NotNull String criterion) {
        if(!(e instanceof FileObject base)) return null;
        if(!(base.get("objective") instanceof FileObject o)) return null;

        Key type = o.getObject(Key.class, "type");
        if(type==null) return null;
        CustomFileAdvancementObjective<?> handler = CUSTOM_HANDLERS.get(type);
        if(handler==null) throw new IllegalStateException("AdvancementObject type " + type + " does not exist!");

        LootCondition conditions = ctx.getRegistry().deserializeFromFile(LootCondition.class, base.get("conditions"));

        return handler.deserializeFromFile(ctx, o, new ObjectiveCommonData(criterion, conditions));
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "advancement_objective";
    }
}
