package killercreepr.crux.item.components;

import killercreepr.crux.util.CruxItem;
import killercreepr.crux.valueproviders.InputContext;
import org.jetbrains.annotations.NotNull;

public class DynamicItemLore extends DynamicSingleValueComponent {
    public DynamicItemLore(@NotNull Object value) {
        super(value);
    }

    @Override
    public @NotNull String name() {
        return "lore";
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull InputContext context) {
        item.loreFromString(parseStringList(context));
    }
}
