package killercreepr.cruxconfig.config.bukkit.handler.impl.entity.component;

import killercreepr.crux.api.entity.dynamic.DynamicEntityApplierSingleComponent;
import killercreepr.crux.core.item.dynamic.component.DynamicSingleValueComponent;
import killercreepr.cruxconfig.config.bukkit.handler.impl.item.component.FileDynamicItemComponent;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class FileSingleDynamicEntityApplierComponent<T extends DynamicEntityApplierSingleComponent> extends FileDynamicEntityApplierComponent<T> {
    protected FileSingleDynamicEntityApplierComponent(@NotNull Class<T> type) {
        super(type);
    }

    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull T object) {
        return context.getRegistry().serializeToFile(object.getValue());
    }

    @Override
    public @Nullable T deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        return deserialize(context, e);
    }

    public @Nullable T deserialize(@NotNull FileContext<?>  context, @NotNull FileElement value){
        Object o = context.getRegistry().deserializeObjectFromFile(value);
        if(o == null) return null;
        return deserialize(o);
    }

    public abstract T deserialize(Object value);
}
