package killercreepr.cruxadvancements.config.handler;

import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.SimpleMappedRegistry;
import killercreepr.cruxadvancements.advancement.objective.condition.ObjectiveCondition;
import killercreepr.cruxconfig.config.bukkit.handler.FileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileObjectiveCondition implements FileHandler<ObjectiveCondition> {
    public static final MappedRegistry<String, CustomFileObjectiveCondition<?>> CUSTOM_HANDLERS = new SimpleMappedRegistry<>();
    public static void registerCustomHandler(@NotNull CustomFileObjectiveCondition<?> handler){
        CUSTOM_HANDLERS.register(handler.getType(), handler);
    }
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull ObjectiveCondition object) {
        throw new RuntimeException("unsupported");
    }

    @Override
    public @Nullable ObjectiveCondition deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        String type = o.getObject(String.class, "type");
        if(type==null) return null;
        String target = o.getObject(String.class, "target");
        return deserializeFromFile(context, e, target);
    }

    public static @Nullable ObjectiveCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e, @Nullable String target) {
        if(!(e instanceof FileObject o)) return null;
        String type = o.getObject(String.class, "type");
        if(type==null) return null;
        type = type.toLowerCase();
        CustomFileObjectiveCondition<?> handler = CUSTOM_HANDLERS.get(type);
        if(handler==null) throw new IllegalStateException("ObjectiveCondition type " + type + " does not exist!");
        return handler.deserializeFromFile(ctx, o, target == null ? "this" : target);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "objective_condition";
    }
}
