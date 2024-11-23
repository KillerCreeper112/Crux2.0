package killercreepr.crux.core.item.dynamic.component;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.item.dynamic.DynamicItemComponent;
import killercreepr.crux.api.text.context.TextParserContext;
import org.jetbrains.annotations.NotNull;

public class DynamicItemName extends DynamicSingleValueComponent{
    public DynamicItemName(@NotNull Object value) {
        super(value);
    }

    @Override
    public @NotNull String name() {
        return "name";
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull TextParserContext context) {
        item.itemName(parseComponent(context));
    }

    @Override
    public @NotNull DynamicItemComponent merge(@NotNull DynamicItemComponent with) {
        if(!(with instanceof DynamicItemName n)) return this;
        String add = n.getValue().toString();
        if(add.isEmpty()) return this;
        return new DynamicItemName(value + add);
    }
}
