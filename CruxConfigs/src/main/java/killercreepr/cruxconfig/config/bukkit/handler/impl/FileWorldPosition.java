package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.api.data.WorldPosition;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.data.SimpleWorldPosition;
import killercreepr.crux.core.location.SimplePositionResolver;
import killercreepr.crux.core.util.CruxCollection;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import net.kyori.adventure.key.Key;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@JsonSerializer(id = "world_position")
public class FileWorldPosition extends SimpleFileHandler<WorldPosition> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull WorldPosition vector) {
        var o = new FileObject()
            .addProperty("x", vector.x())
            .addProperty("y", vector.y())
            .addProperty("z", vector.z())
            ;
        if(vector.worldKey() != null){
            o.add("world", context.getRegistry().serializeToFile(vector.worldKey()));
        }
        return o;
    }

    @Override
    public @Nullable WorldPosition deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        FileRegistry registry = context.getRegistry();
        if(!(e instanceof FileObject o)){
            if(e instanceof FileArray a){
                int index = -1;
                var list = new ArrayList<Double>();
                for (FileElement aEle : a.asList()) {
                    index++;
                    if(index > 2) break;
                    var value = registry.deserializeFromFile(Double.class, aEle, context);
                    list.add(value);
                }
                return new SimpleWorldPosition(
                  CruxCollection.getOrDefaultNonNull(list, 0, 0D),
                  CruxCollection.getOrDefaultNonNull(list, 1, 0D),
                  CruxCollection.getOrDefaultNonNull(list, 2, 0D),
                  index == 3 ? registry.deserializeFromFile(Key.class, a.get(index)) : null
                );
            }
            return null;
        }
        Double x = registry.deserializeFromFileOrDefault(Double.class, o.get("x"), 0D);
        Double y = registry.deserializeFromFileOrDefault(Double.class, o.get("y"), 0D);
        Double z = registry.deserializeFromFileOrDefault(Double.class, o.get("z"), 0D);
        return new SimpleWorldPosition(
          x == null ? 0D : x, y == null ? 0D : y, z == null ? 0D : z,
          registry.deserializeFromFile(Key.class, o.get("world"))
        );
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "world_position";
    }
}
