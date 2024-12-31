package killercreepr.crux.core.item.dynamic.component;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.item.dynamic.DynamicItemComponent;
import killercreepr.crux.api.text.context.TextParserContext;
import org.jetbrains.annotations.NotNull;

public class DynamicItemCustomName extends DynamicSingleValueComponent{
    public DynamicItemCustomName(@NotNull Object value) {
        super(value);
    }

    @Override
    public @NotNull String name() {
        return "display_name";
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull TextParserContext context) {
        item.customName(CruxItem.NO_ITALICS.append(parseComponent(context)).compact());
    }

    @Override
    public @NotNull DynamicItemComponent merge(@NotNull DynamicItemComponent with) {
        if(!(with instanceof DynamicItemCustomName n)) return this;
        String add = n.getValue().toString();
        if(add.isEmpty()) return this;
        return new DynamicItemCustomName(value + add);
    }
}
