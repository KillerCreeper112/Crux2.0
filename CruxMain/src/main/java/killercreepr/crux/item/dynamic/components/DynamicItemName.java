package killercreepr.crux.item.dynamic.components;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.util.CruxItem;
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
    public void apply(@NotNull CruxItem item, @NotNull TextParserContext context) {
        item.itemName(parseComponent(context));
    }
}
