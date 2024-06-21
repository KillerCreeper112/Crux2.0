package killercreepr.crux.item.dynamic.components;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.util.CruxColor;
import killercreepr.crux.util.CruxItem;
import org.jetbrains.annotations.NotNull;

public class DynamicItemColor extends DynamicSingleValueComponent{
    public DynamicItemColor(@NotNull Object value) {
        super(value);
    }

    @Override
    public @NotNull String name() {
        return "color";
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull TextParserContext context) {
        item.color(CruxColor.hexToColor(parseString(context)));
    }
}
