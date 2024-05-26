package killercreepr.crux.item.components;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.util.CruxItem;
import org.jetbrains.annotations.NotNull;

public class DynamicItemMaxStackSize extends DynamicSingleValueComponent{
    public DynamicItemMaxStackSize(@NotNull Object value) {
        super(value);
    }

    @Override
    public @NotNull String name() {
        return "max_stack_size";
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull TextParserContext context) {
        item.editMeta(meta -> meta.setMaxStackSize(parseInt(context)));
    }
}
