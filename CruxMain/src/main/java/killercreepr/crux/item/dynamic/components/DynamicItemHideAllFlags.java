package killercreepr.crux.item.dynamic.components;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.util.CruxItem;
import killercreepr.crux.util.CruxString;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

public class DynamicItemHideAllFlags extends DynamicSingleValueComponent {
    public DynamicItemHideAllFlags(@NotNull Object value) {
        super(value);
    }

    @Override
    public @NotNull String name() {
        return "hide_flags";
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull TextParserContext context) {
        if(CruxString.parseBoolean(parseString(context))){
            item.addFlags(ItemFlag.values());
        }else item.removeFlags(ItemFlag.values());
    }
}
