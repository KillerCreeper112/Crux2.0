package killercreepr.crux.item.components;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.util.CruxItem;
import org.jetbrains.annotations.NotNull;

public class DynamicItemHideTooltip extends DynamicSingleValueComponent{
    public DynamicItemHideTooltip(@NotNull Object value) {
        super(value);
    }

    @Override
    public @NotNull String name() {
        return "hide_tooltip";
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull TextParserContext context) {
        item.editMeta(meta -> meta.setHideTooltip(parseBool(context)));
    }
}
