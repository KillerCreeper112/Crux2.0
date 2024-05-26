package killercreepr.crux.item.components;

import killercreepr.crux.util.CruxItem;
import killercreepr.crux.valueproviders.InputContext;
import org.jetbrains.annotations.NotNull;

public class DynamicItemFireResistant extends DynamicSingleValueComponent{
    public DynamicItemFireResistant(@NotNull Object value) {
        super(value);
    }

    @Override
    public @NotNull String name() {
        return "fire_resistant";
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull InputContext context) {
        item.editMeta(meta -> meta.setFireResistant(parseBool(context)));
    }
}
