package killercreepr.crux.item;

import killercreepr.crux.util.CruxItem;
import killercreepr.crux.valueproviders.InputContext;
import org.jetbrains.annotations.NotNull;

public interface DynamicItemComponent {
    @NotNull String name();
    void apply(@NotNull CruxItem item, @NotNull InputContext context);
}
