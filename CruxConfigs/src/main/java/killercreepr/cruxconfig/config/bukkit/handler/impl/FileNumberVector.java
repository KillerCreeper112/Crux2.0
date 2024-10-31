package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.util.CruxObjects;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.crux.valueproviders.vector.NumberVector;
import killercreepr.crux.valueproviders.vector.SimpleNumberVector;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonSerializer(id = "number_vector")
public class FileNumberVector extends SimpleFileHandler<NumberVector> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull NumberVector object) {
        FileRegistry registry = context.getRegistry();
        return new FileObject()
            .add("x", registry.serializeToFile(object.x()))
            .add("y", registry.serializeToFile(object.y()))
            .add("z", registry.serializeToFile(object.z()))
            ;
        //throw new UnsupportedOperationException("Oof");
    }

    @Override
    public @Nullable NumberVector deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = context.getRegistry();
        NumberProvider x = registry.deserializeFromFile(NumberProvider.class, o.get("x"));
        NumberProvider y = registry.deserializeFromFile(NumberProvider.class, o.get("y"));
        NumberProvider z = registry.deserializeFromFile(NumberProvider.class, o.get("z"));
        if(CruxObjects.checkNull(x, y, z)) return null;
        return new SimpleNumberVector(x, y, z);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "number_vector";
    }
}
