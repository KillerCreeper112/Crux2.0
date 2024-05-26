package killercreepr.crux.item.components;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.util.CruxItem;
import org.jetbrains.annotations.NotNull;

public class DynamicItemUnbreakable extends DynamicSingleValueComponent{
    public DynamicItemUnbreakable(@NotNull Object value) {
        super(value);
    }

    @Override
    public @NotNull String name() {
        return "unbreakable";
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull TextParserContext context) {
        item.unbreakable(parseBool(context));
    }
}
