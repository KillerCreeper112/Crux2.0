package killercreepr.cruxconfig.config.bukkit.handler.impl.item.component;

import killercreepr.crux.item.dynamic.components.DynamicSingleValueComponent;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class FileGenericSingleDynamicComponent<T extends DynamicSingleValueComponent> extends FileSingleDynamicComponent<T> {
    protected FileGenericSingleDynamicComponent(@NotNull Class<T> type) {
        super(type);
    }

    @Override
    public @Nullable T deserialize(@NotNull FileContext<?> context, @NotNull FileElement value) {
        return deserialize(context.getRegistry().deserializeObject(value));
    }

    public abstract @Nullable T deserialize(@NotNull Object object);
}
