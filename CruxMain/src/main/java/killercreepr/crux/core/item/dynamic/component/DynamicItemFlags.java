package killercreepr.crux.core.item.dynamic.component;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.item.dynamic.DynamicItemComponent;
import killercreepr.crux.api.text.context.TextParserContext;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

//todo possibly make it a map: Flag -> True/False
public class DynamicItemFlags extends SimpleDynamicItemComponent {
    protected final @NotNull Collection<Object> flags;
    public DynamicItemFlags(@NotNull Collection<Object> flags) {
        this.flags = flags;
    }

    @Override
    public @NotNull String name() {
        return "flags";
    }

    public @NotNull Collection<Object> getFlags() {
        return flags;
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull TextParserContext context) {
        flags.forEach((flagObject) ->{
            ItemFlag flag;
            try{
                flag = ItemFlag.valueOf(context.deserializeString(flagObject.toString()).toUpperCase());
            }catch (IllegalArgumentException ignored){
                return;
            }
            item.addFlags(flag);
        });
    }
}
