package killercreepr.crux.core.item.dynamic.component;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.util.CruxString;
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
        boolean x = value instanceof Boolean ? (Boolean) value : CruxString.parseBoolean(parseString(context));
        if(x){
            item.addFlags(ItemFlag.values()).hideAttributes();
        }else item.removeFlags(ItemFlag.values());
    }
}
