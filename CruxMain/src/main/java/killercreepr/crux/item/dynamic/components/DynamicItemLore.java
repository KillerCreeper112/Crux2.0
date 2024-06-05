package killercreepr.crux.item.dynamic.components;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.util.CruxItem;
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
    public void apply(@NotNull CruxItem item, @NotNull TextParserContext context) {
        item.lore(parseComponentList(context));
    }
}
