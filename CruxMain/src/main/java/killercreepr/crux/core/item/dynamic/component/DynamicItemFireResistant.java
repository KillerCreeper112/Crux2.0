package killercreepr.crux.core.item.dynamic.component;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.text.context.TextParserContext;
import org.jetbrains.annotations.NotNull;

public class DynamicItemFireResistant extends DynamicSingleValueComponent{
    public DynamicItemFireResistant(@NotNull Object value) {
        super(value);
    }

    @Override
    public @NotNull String name() {
        return "fire_resistant";
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull TextParserContext context) {
        item.editMeta(meta -> meta.setFireResistant(parseBool(context)));
    }
}
