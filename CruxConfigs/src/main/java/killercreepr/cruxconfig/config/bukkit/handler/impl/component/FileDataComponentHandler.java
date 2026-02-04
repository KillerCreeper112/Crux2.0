package killercreepr.cruxconfig.config.bukkit.handler.impl.component;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.component.DataComponentAccessor;
import killercreepr.crux.api.component.DataComponentHandler;
import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class FileDataComponentHandler implements FileObjectHandler<DataComponentHandler> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull DataComponentHandler object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable DataComponentHandler deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        Collection<TypedDataComponent<?>> list = ctx.getRegistry().deserializeFromFile(
            new TypeToken<Collection<TypedDataComponent>>(){}.getType(), e, ctx
        );
        return DataComponentHandler.simple(list);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "data_component_handler";
    }
}
