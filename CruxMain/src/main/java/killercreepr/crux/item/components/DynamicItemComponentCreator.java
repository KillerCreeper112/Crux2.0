package killercreepr.crux.item.components;

import killercreepr.crux.item.DynamicItemComponent;
import org.jetbrains.annotations.NotNull;

public interface DynamicItemComponentCreator {
    @NotNull DynamicItemComponent create(@NotNull String value);
}
