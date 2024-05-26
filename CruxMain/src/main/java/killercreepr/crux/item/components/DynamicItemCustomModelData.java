package killercreepr.crux.item.components;

import killercreepr.crux.util.CruxItem;
import killercreepr.crux.valueproviders.InputContext;
import org.jetbrains.annotations.NotNull;

public class DynamicItemCustomModelData extends DynamicSingleValueComponent{
    public DynamicItemCustomModelData(@NotNull Object value) {
        super(value);
    }

    @Override
    public @NotNull String name() {
        return "custom_model_data";
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull InputContext context) {
        item.editMeta(meta -> meta.setCustomModelData(parseInt(context)));
    }
}
