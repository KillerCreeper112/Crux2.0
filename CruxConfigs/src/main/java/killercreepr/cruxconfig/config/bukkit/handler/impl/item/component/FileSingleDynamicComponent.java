package killercreepr.cruxconfig.config.bukkit.handler.impl.item.component;

import killercreepr.crux.item.components.DynamicSingleValueComponent;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class FileSingleDynamicComponent<T extends DynamicSingleValueComponent> extends FileDynamicItemComponent<T> {
    protected FileSingleDynamicComponent(@NotNull Class<T> type) {
        super(type);
    }

    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull T object) {
        return context.getRegistry().serializeToFileElement(object.getValue());
    }

    @Override
    public @Nullable T deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        return deserialize(context, e);
    }

    public abstract @Nullable T deserialize(@NotNull FileContext<?>  context, @NotNull FileElement value);
}
