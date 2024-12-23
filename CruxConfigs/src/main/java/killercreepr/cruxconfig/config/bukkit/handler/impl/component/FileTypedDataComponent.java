package killercreepr.cruxconfig.config.bukkit.handler.impl.component;

import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.crux.api.registry.MappedRegistry;
import killercreepr.crux.core.registry.SimpleMappedRegistry;
import killercreepr.cruxconfig.config.bukkit.registry.FileDataComponentRegistry;
import killercreepr.cruxconfig.config.bukkit.registry.SimpleFileDataComponentRegistry;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileTypedDataComponent implements FileObjectHandler<TypedDataComponent<?>> {
    protected final FileDataComponentRegistry TYPE_HANDLERS = new SimpleFileDataComponentRegistry();
    public FileDataComponentRegistry typeHandlers(){
        return TYPE_HANDLERS;
    }
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull TypedDataComponent<?> object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable TypedDataComponent<?> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        Key type = o.getObject(Key.class, "component");
        if(type == null) return null;
        FileDataComponentType<?> file = typeHandlers().get(type);
        if(file == null) throw new IllegalArgumentException("FileDataComponentType of " + type + " does not exist!");
        return file.deserializeFromFile(ctx, o);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "typed_data_component";
    }
}
