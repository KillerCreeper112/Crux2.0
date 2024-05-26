package killercreepr.crux.item.components;

import killercreepr.crux.util.CruxItem;
import killercreepr.crux.valueproviders.InputContext;
import org.bukkit.inventory.ItemRarity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DynamicItemRarity extends DynamicSingleValueComponent{
    public DynamicItemRarity(@NotNull Object value) {
        super(value);
    }

    @Override
    public @NotNull String name() {
        return "rarity";
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull InputContext context) {
        item.editMeta(meta -> meta.setRarity(parseObject(context)));
    }

    public @Nullable ItemRarity parseObject(@NotNull InputContext context){
        try{
            return ItemRarity.valueOf(parse(context).toUpperCase());
        }catch (IllegalArgumentException ignored){ return null; }
    }
}
