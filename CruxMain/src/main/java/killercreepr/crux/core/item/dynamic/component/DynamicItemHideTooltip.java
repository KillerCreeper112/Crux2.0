package killercreepr.crux.core.item.dynamic.component;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.text.context.TextParserContext;
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
