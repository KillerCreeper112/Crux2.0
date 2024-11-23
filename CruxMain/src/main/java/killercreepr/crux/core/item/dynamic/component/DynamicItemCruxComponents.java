package killercreepr.crux.core.item.dynamic.component;

import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.crux.api.component.parser.DataComponentDecoder;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.item.SimpleCruxItem;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class DynamicItemCruxComponents extends DynamicSingleValueComponent{
    public DynamicItemCruxComponents(@NotNull Object value) {
        super(value);
    }

    @Override
    public @NotNull String name() {
        return "crux_components";
    }

    @Override
    public void apply(@NotNull SimpleCruxItem item, @NotNull TextParserContext context) {
        Collection<TypedDataComponent<?>> parsed = DataComponentDecoder.componentDecoder().parseComponents(parseString(context));
        parsed.forEach(item::set);
    }
}
