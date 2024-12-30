package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@JsonSerializer(id = "vector")
public class FileVector extends SimpleFileHandler<Vector> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull Vector vector) {
        return new FileObject()
            .addProperty("x", vector.getX())
            .addProperty("y", vector.getY())
            .addProperty("z", vector.getZ())
            ;
    }

    @Override
    public @Nullable Vector deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)){
            if(e instanceof FileArray a){
                List<Number> list = new ArrayList<>();
                a.forEach(ele ->{
                    FileGeneric generic = ele.getAsFilePrimitive();
                    if(generic.isString()) list.add(CruxMath.evaluate(generic.getAsString()));
                    else list.add(generic.getAsNumber());
                });
                if(list.isEmpty()) return new Vector();
                return new Vector(
                    list.get(0).doubleValue(),
                    list.size() > 1 ? list.get(1).doubleValue() : 0D,
                    list.size() > 2 ? list.get(2).doubleValue() : 0D
                );
            }
            return null;
        }
        FileRegistry registry = context.getRegistry();
        Number x = registry.deserializeFromFile(Number.class, o.get("x"));
        Number y = registry.deserializeFromFile(Number.class, o.get("y"));
        Number z = registry.deserializeFromFile(Number.class, o.get("z"));
        return new Vector(x == null ? 0D : x.doubleValue(), y == null ? 0D : y.doubleValue(), z == null ? 0D : z.doubleValue());
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "vector";
    }
}
