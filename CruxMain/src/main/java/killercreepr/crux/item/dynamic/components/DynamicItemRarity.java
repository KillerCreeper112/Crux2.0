package killercreepr.crux.item.dynamic.components;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.util.CruxItem;
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
    public void apply(@NotNull CruxItem item, @NotNull TextParserContext context) {
        item.editMeta(meta -> meta.setRarity(parseObject(context)));
    }

    public @Nullable ItemRarity parseObject(@NotNull TextParserContext context){
        try{
            return ItemRarity.valueOf(parseString(context).toUpperCase());
        }catch (IllegalArgumentException ignored){ return null; }
    }
}
