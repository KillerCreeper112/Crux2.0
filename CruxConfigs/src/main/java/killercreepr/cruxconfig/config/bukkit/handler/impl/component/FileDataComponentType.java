package killercreepr.cruxconfig.config.bukkit.handler.impl.component;

import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FileDataComponentType<T> {
    default @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull TypedDataComponent<T> object){
        throw new UnsupportedOperationException("unsupported");
    }
    @Nullable TypedDataComponent<T> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e);
}
