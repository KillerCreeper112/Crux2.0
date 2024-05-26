package killercreepr.crux.item.components;

import killercreepr.crux.util.CruxItem;
import killercreepr.crux.valueproviders.InputContext;
import org.jetbrains.annotations.NotNull;

public class DynamicItemName extends DynamicSingleValueComponent{
    public DynamicItemName(@NotNull Object value) {
        super(value);
    }

    @Override
    public @NotNull String name() {
        return "name";
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull InputContext context) {
        item.displayName(parse(context));
    }
}
