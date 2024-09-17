package killercreepr.cruxconfig.config.bukkit.handler.impl.component;

import killercreepr.crux.component.TypedDataComponent;
import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.SimpleMappedRegistry;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileTypedDataComponent implements FileObjectHandler<TypedDataComponent<?>> {
    protected final MappedRegistry<String, FileDataComponentType<?>> TYPE_HANDLERS = new SimpleMappedRegistry<>();
    public MappedRegistry<String, FileDataComponentType<?>> typeHandlers(){
        return TYPE_HANDLERS;
    }
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull TypedDataComponent<?> object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable TypedDataComponent<?> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        String type = o.getObject(String.class, "component");
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
