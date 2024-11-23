package killercreepr.crux.core.item.dynamic.component;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.text.context.TextParserContext;
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
