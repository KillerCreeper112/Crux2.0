package killercreepr.crux.item.dynamic.components;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.util.CruxItem;
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
