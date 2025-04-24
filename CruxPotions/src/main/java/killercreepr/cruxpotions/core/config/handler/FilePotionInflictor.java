package killercreepr.cruxpotions.core.config.handler;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxpotions.api.potion.inflictor.PotionInflictor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class FilePotionInflictor implements FileObjectHandler<PotionInflictor> {
    protected final Map<String, FileObjectHandler<? extends PotionInflictor>> registry = new HashMap<>();
    public void register(String type, FileObjectHandler<? extends PotionInflictor> handler){
        registry.put(type, handler);
    }
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull PotionInflictor object) {
        FileObjectHandler handler = registry.get(object.getTypeID());
        if(handler == null) throw new UnsupportedOperationException(object + " cannot be serialized!");
        FileObject o = new FileObject();
        o.addProperty("potion_inflictor_type", object.getTypeID());
        o.add("value", handler.serializeToFile(ctx, object));
        return o;
    }

    @Override
    public @Nullable PotionInflictor deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        String type = o.getObject(String.class, "potion_inflictor_type");
        if(type == null) return null;
        var handler = registry.get(type);
        if(handler == null) throw new RuntimeException("PotionInflictorType " + type + " not found!");
        return handler.deserializeFromFile(ctx, o.get("value"));
    }
}
