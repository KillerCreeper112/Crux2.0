package killercreepr.cruxadvancements.config.handler;

import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.SimpleMappedRegistry;
import killercreepr.cruxadvancements.advancement.objective.AdvancementObjective;
import killercreepr.cruxadvancements.advancement.objective.condition.ObjectiveConditions;
import killercreepr.cruxconfig.config.bukkit.handler.FileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileAdvancementObjective implements FileHandler<AdvancementObjective> {
    public static final MappedRegistry<String, CustomFileAdvancementObjective<?>> CUSTOM_HANDLERS = new SimpleMappedRegistry<>();
    public static void registerCustomHandler(@NotNull CustomFileAdvancementObjective<?> handler){
        CUSTOM_HANDLERS.register(handler.getType(), handler);
    }
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull AdvancementObjective object) {
        throw new RuntimeException("todo");
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

        String type = o.getObject(String.class, "type");
        if(type==null) return null;
        type = type.toLowerCase();
        CustomFileAdvancementObjective<?> handler = CUSTOM_HANDLERS.get(type);
        if(handler==null) throw new IllegalStateException("AdvancementObject type " + type + " does not exist!");

        ObjectiveConditions conditions = ctx.getRegistry().deserialize(ObjectiveConditions.class, base.get("conditions"));

        return handler.deserializeFromFile(ctx, o, criterion, conditions);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "advancement_objective";
    }
}
