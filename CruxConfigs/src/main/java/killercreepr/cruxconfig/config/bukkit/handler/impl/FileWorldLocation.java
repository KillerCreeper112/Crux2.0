package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.api.data.WorldLocation;
import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.data.SimpleWorldLocation;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.*;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@JsonSerializer(id = "world_location")
public class FileWorldLocation extends SimpleFileHandler<WorldLocation> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull WorldLocation loc) {
        if(!(context.getRegistry().serializeToFile(loc.location()) instanceof FileObject o)){
            return FileNull.INSTANCE;
        }
        return o.add("world", context.getRegistry().serializeToFile(loc.worldKey()));
    }

    @Override
    public @Nullable WorldLocation deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)){
            if(e instanceof FileArray a){
                List<Number> list = new ArrayList<>();
                String world = null;
                for (FileElement ele : a) {
                    if(list.size() > 4){
                        world = ele.getAsString();
                        break;
                    }

                    FileGeneric generic = ele.getAsFilePrimitive();
                    if(generic.isString()) list.add(CruxMath.evaluate(generic.getAsString()));
                    else list.add(generic.getAsNumber());
                }
                if(list.isEmpty()) return WorldLocation.worldLocation(0D, 0D, 0D, 0f, 0f, null);
                return WorldLocation.worldLocation(
                    list.get(0).doubleValue(),
                    list.size() > 1 ? list.get(1).doubleValue() : 0D,
                    list.size() > 2 ? list.get(2).doubleValue() : 0D,
                    //yaw, pitch
                    list.size() > 3 ? list.get(3).floatValue() : 0f,
                    list.size() > 4 ? list.get(4).floatValue() : 0f,
                    world == null ? null : Crux.key(world)
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
        return WorldLocation.worldLocation(
            x == null ? 0D : x.doubleValue(), y == null ? 0D : y.doubleValue(), z == null ? 0D : z.doubleValue(),
            yaw == null ? 0f : yaw.floatValue(),
            pitch == null ? 0f : pitch.floatValue(),
            registry.deserializeFromFile(Key.class, o.get("world"))
        );
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "world_location";
    }
}
