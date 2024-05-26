package killercreepr.crux.item;

import killercreepr.crux.util.CruxItem;
import killercreepr.crux.valueproviders.InputContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface DynamicItem {
    @NotNull String material();
    @NotNull String amount();
    @Nullable Map<String, DynamicItemComponent> components();

    @NotNull DynamicItem withType(@NotNull String material);
    @NotNull DynamicItem withAmount(@NotNull String amount);
    @NotNull DynamicItem withComponent(@NotNull DynamicItemComponent component);

    @Nullable
    CruxItem build(@NotNull InputContext context);
}
