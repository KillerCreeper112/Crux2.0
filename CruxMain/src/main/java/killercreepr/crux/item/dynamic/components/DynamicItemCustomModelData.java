package killercreepr.crux.item.dynamic.components;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.util.CruxItem;
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
    public void apply(@NotNull CruxItem item, @NotNull TextParserContext context) {
        item.editMeta(meta -> meta.setCustomModelData(parseInt(context)));
    }
}
