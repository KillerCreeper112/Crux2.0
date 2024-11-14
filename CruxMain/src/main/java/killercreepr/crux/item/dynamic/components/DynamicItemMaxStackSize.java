package killercreepr.crux.item.dynamic.components;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.util.CruxItem;
import killercreepr.crux.util.CruxMath;
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
        item.editMeta(meta -> meta.setMaxStackSize(CruxMath.clamp(parseInt(context), 1, 99)));
    }
}
