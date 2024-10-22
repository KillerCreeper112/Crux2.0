package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.util.CruxObjects;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = context.getRegistry();
        Number x = registry.deserializeFromFile(Number.class, o.get("x"));
        Number y = registry.deserializeFromFile(Number.class, o.get("y"));
        Number z = registry.deserializeFromFile(Number.class, o.get("z"));
        if(CruxObjects.checkNull(x, y, z)) return null;
        return new Vector(x.doubleValue(), y.doubleValue(), z.doubleValue());
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "vector";
    }
}
