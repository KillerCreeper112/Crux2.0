package killercreepr.crux.core.item.dynamic.component;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.item.SimpleCruxItem;
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
    public void apply(@NotNull SimpleCruxItem item, @NotNull TextParserContext context) {
        item.unbreakable(parseBool(context));
    }
}
