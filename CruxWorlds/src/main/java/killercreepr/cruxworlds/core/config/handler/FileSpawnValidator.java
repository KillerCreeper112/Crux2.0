package killercreepr.cruxworlds.core.config.handler;

import killercreepr.crux.api.registry.MappedRegistry;
import killercreepr.crux.core.registry.SimpleMappedRegistry;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxworlds.api.world.spawning.SpawnValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileSpawnValidator implements FileObjectHandler<SpawnValidator> {
    private final MappedRegistry<String, FileObjectHandler<? extends SpawnValidator>> TYPE_HANDLERS = new SimpleMappedRegistry<>();
    public MappedRegistry<String, FileObjectHandler<? extends SpawnValidator>> typeHandlers(){
        return TYPE_HANDLERS;
    }

    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull SpawnValidator object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable SpawnValidator deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        String type;
        if(e instanceof FileObject o){
            type = o.getObject(String.class, "spawn_condition");
        }else if(e instanceof FileGeneric g){
            type = g.getAsString();
        }else return null;
        if(type==null) return null;
        FileObjectHandler<? extends  SpawnValidator> handler = TYPE_HANDLERS.get(type);
        if(handler==null) throw new RuntimeException("No type handler found for " + type + "!");
        return handler.deserializeFromFile(context, e);
    }
}
