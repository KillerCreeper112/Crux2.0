package killercreepr.crux.core.item.dynamic.component;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.DyedItemColor;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.util.CruxColor;
import org.jetbrains.annotations.NotNull;

public class DynamicItemDyedColor extends DynamicSingleValueComponent{
    public DynamicItemDyedColor(@NotNull Object value) {
        super(value);
    }

    @Override
    public @NotNull String name() {
        return "dyed_color";
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull TextParserContext context) {
        try{
            item.item().setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor(CruxColor.hexToColor(parseString(context))));
        }catch (IllegalArgumentException ignored){

        }
    }
}
