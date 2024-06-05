package killercreepr.crux.item.dynamic.components;

import killercreepr.crux.item.dynamic.DynamicItemComponent;
import org.jetbrains.annotations.NotNull;

public interface DynamicItemComponentCreator {
    @NotNull DynamicItemComponent create(@NotNull String value);
}
