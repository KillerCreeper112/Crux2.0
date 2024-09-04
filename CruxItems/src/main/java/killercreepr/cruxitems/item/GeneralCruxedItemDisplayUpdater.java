package killercreepr.cruxitems.item;

import killercreepr.crux.Crux;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GeneralCruxedItemDisplayUpdater implements CruxedItemUpdater{
    public static final Key KEY = Crux.key("general_item_display");
    protected final @NotNull ItemDisplayFormatter formatter;
    public GeneralCruxedItemDisplayUpdater(@NotNull ItemDisplayFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public void onUpdate(@NotNull CruxedItemUpdateContext context) {
        CruxedItem item = context.getItem();
        ItemStack i = item.item();
        String name = formatter.getNameFormat(i);
        if(name != null){
            item.itemName(name);
        }
        List<String> lore = formatter.getLoreFormat(i);
        if(lore != null){
            item.loreFromString(lore);
        }
    }

    @Override
    public @NotNull Key key() {
        return KEY;
    }
}
