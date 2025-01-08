package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.core.util.CruxLoc;
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

@JsonSerializer(id = "crux_location")
public class FileCruxLocation extends SimpleFileHandler<CruxLocation> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull CruxLocation loc) {
        return new FileObject()
            .addProperty("x", loc.x())
            .addProperty("y", loc.y())
            .addProperty("z", loc.z())
            .addProperty("yaw", loc.yaw())
            .addProperty("pitch", loc.pitch())
            ;
    }

    @Override
    public @Nullable CruxLocation deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)){
            if(e instanceof FileArray a){
                List<Number> list = new ArrayList<>();
                a.forEach(ele ->{
                    FileGeneric generic = ele.getAsFilePrimitive();
                    if(generic.isString()) list.add(CruxMath.evaluate(generic.getAsString()));
                    else list.add(generic.getAsNumber());
                });
                if(list.isEmpty()) return CruxLocation.location(0D, 0D, 0D);
                return CruxLocation.location(
                    list.get(0).doubleValue(),
                    list.size() > 1 ? list.get(1).doubleValue() : 0D,
                    list.size() > 2 ? list.get(2).doubleValue() : 0D,
                    //yaw, pitch
                    list.size() > 3 ? list.get(3).floatValue() : 0f,
                    list.size() > 4 ? list.get(4).floatValue() : 0f
                );
            }
            return null;
        }
        FileRegistry registry = context.getRegistry();
        Number x = registry.deserializeFromFile(Number.class, o.get("x"));
        Number y = registry.deserializeFromFile(Number.class, o.get("y"));
        Number z = registry.deserializeFromFile(Number.class, o.get("z"));
        Number yaw = registry.deserializeFromFile(Number.class, o.get("yaw"));
        Number pitch = registry.deserializeFromFile(Number.class, o.get("pitch"));
        return CruxLocation.location(
            x == null ? 0D : x.doubleValue(), y == null ? 0D : y.doubleValue(), z == null ? 0D : z.doubleValue(),
            yaw == null ? 0f : yaw.floatValue(),
            pitch == null ? 0f : pitch.floatValue()
        );
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "crux_location";
    }
}
