package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.core.math.LocationPos;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@JsonSerializer(id = "location_pos")
public class FileLocationPos extends SimpleFileHandler<LocationPos> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull LocationPos vector) {
        return new FileObject()
            .addProperty("x", vector.x())
            .addProperty("y", vector.y())
            .addProperty("z", vector.z())
            ;
    }

    @Override
    public @Nullable LocationPos deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)){
            if(e instanceof FileArray a){
                List<Number> list = new ArrayList<>();
                a.forEach(ele ->{
                    FileGeneric generic = ele.getAsFilePrimitive();
                    if(generic.isString()) list.add(CruxMath.evaluate(generic.getAsString()));
                    else list.add(generic.getAsNumber());
                });
                if(list.isEmpty()) return new LocationPos(0,0,0);
                return new LocationPos(
                    list.get(0).doubleValue(),
                    list.size() > 1 ? list.get(1).doubleValue() : 0,
                    list.size() > 2 ? list.get(2).doubleValue() : 0
                );
            }
            return null;
        }
        FileRegistry registry = context.getRegistry();
        Number x = registry.deserializeFromFile(Number.class, o.get("x"));
        Number y = registry.deserializeFromFile(Number.class, o.get("y"));
        Number z = registry.deserializeFromFile(Number.class, o.get("z"));
        return new LocationPos(x == null ? 0 : x.doubleValue(), y == null ? 0 : y.doubleValue(), z == null ? 0 : z.doubleValue());
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "location_pos";
    }
}
