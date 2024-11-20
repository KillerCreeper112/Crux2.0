package killercreepr.crux.core.item.dynamic.component;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.util.CruxColor;
import killercreepr.crux.core.util.CruxItem;
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
