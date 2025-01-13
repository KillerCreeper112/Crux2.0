package killercreepr.crux.core.item.dynamic.component;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.Crux;
import org.jetbrains.annotations.NotNull;

public class DynamicItemItemModel extends DynamicSingleValueComponent{
    public DynamicItemItemModel(@NotNull Object value) {
        super(value);
    }

    @Override
    public @NotNull String name() {
        return "item_model";
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull TextParserContext context) {
        item.itemModel(Crux.key(parseString(context)));
    }
}
