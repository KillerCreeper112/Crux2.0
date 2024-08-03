package killercreepr.cruxadvancements.config.handler;

import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.Registry;
import killercreepr.crux.registry.SimpleMappedRegistry;
import killercreepr.cruxadvancements.advancement.objective.progress.ObjectiveProgress;
import killercreepr.cruxconfig.config.bukkit.handler.FileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class FileObjectiveProgress implements FileHandler<ObjectiveProgress> {
    protected final @NotNull MappedRegistry<Class<?>, CustomFileObjectiveProgress<?>> CUSTOM_HANDLERS_BY_CLASS = new SimpleMappedRegistry<>();
    protected final @NotNull MappedRegistry<String, CustomFileObjectiveProgress<?>> CUSTOM_HANDLERS_BY_TYPE = new SimpleMappedRegistry<>();

    public @NotNull Registry<CustomFileObjectiveProgress<?>> getCustomHandlers(){
        return CUSTOM_HANDLERS_BY_CLASS;
    }

    public @Nullable CustomFileObjectiveProgress<?> findCustomHandler(@NotNull Class<?> clazz){
        CustomFileObjectiveProgress<?> handler = CUSTOM_HANDLERS_BY_CLASS.get(clazz);
        if(handler!=null) return handler;
        for(Map.Entry<Class<?>, CustomFileObjectiveProgress<?>> entry : CUSTOM_HANDLERS_BY_CLASS.entrySet()){
            if(entry.getKey().isAssignableFrom(clazz)) return entry.getValue();
        }
        return null;
    }

    public void registerCustomHandler(@NotNull Class<?> clazz, @NotNull CustomFileObjectiveProgress<?> handler){
        CUSTOM_HANDLERS_BY_CLASS.register(clazz, handler);
        CUSTOM_HANDLERS_BY_TYPE.register(handler.getType(), handler);
    }

    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull ObjectiveProgress object) {
        CustomFileObjectiveProgress<?> handler = findCustomHandler(object.getClass());
        if(handler == null) throw new IllegalStateException(object + " is not a supported ObjectiveProgress!");
        return handler.attemptSerializeToFile(ctx, object);
    }

    @Override
    public @Nullable ObjectiveProgress deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        String type = o.getObject(String.class, "type");
        if(type==null) return null;
        type = type.toLowerCase();
        CustomFileObjectiveProgress<?> handler = CUSTOM_HANDLERS_BY_TYPE.get(type);
        if(handler == null) throw new IllegalStateException(type + " is not a supported ObjectiveProgress!");
        return handler.deserializeFromFile(ctx, o);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "objective_progress";
    }
}
