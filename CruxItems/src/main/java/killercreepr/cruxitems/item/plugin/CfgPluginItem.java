package killercreepr.cruxitems.item.plugin;

import killercreepr.crux.item.dynamic.DynamicItem;
import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.crux.tags.context.FormatParserContext;
import killercreepr.cruxitems.item.CruxedItem;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CfgPluginItem extends GenericPluginItem{
    protected final @NotNull DynamicItem item;
    public CfgPluginItem(@NotNull Key key, @NotNull DynamicItem item) {
        super(key);
        this.item = item;
    }

    @Override
    public @NotNull CruxedItem build(@Nullable Entity holder, @Nullable MergedTagContainer tags) {
        ItemStack built = item.buildItem(FormatParserContext.builder().tags(tags).build());
        if(built == null) throw new IllegalStateException(key + " plugin item returned NULL!");
        return new CruxedItem(built);
    }
}
