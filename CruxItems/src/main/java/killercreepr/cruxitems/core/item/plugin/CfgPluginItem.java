package killercreepr.cruxitems.core.item.plugin;

import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.core.util.CruxItem;
import killercreepr.cruxitems.api.item.component.InteractableComponent;
import killercreepr.cruxitems.api.item.interaction.InteractableItem;
import killercreepr.cruxitems.api.item.interaction.ItemUseContext;
import killercreepr.cruxitems.api.item.interaction.ItemUseResult;
import killercreepr.cruxitems.core.item.CruxedItem;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class CfgPluginItem extends GenericPluginItem implements InteractableItem {
    protected final @NotNull DynamicItem item;
    public CfgPluginItem(@NotNull Key key, @NotNull DynamicItem item) {
        super(key);
        this.item = item;
    }

    @Override
    public @NotNull CruxedItem build(@Nullable Entity holder, @Nullable MergedTagContainer tags) {
        ItemStack built = item.buildItem(TextParserContext.builder().tags(tags).build());
        if(built == null) throw new IllegalStateException(key + " plugin item returned NULL!");
        return new CruxedItem(built);
    }

    @Override
    public @NotNull ItemUseResult onUse(@NotNull ItemUseContext ctx) {
        CruxItem item = ctx.getItem();
        Collection<InteractableComponent> list = item.getAllOfType(InteractableComponent.class);
        if(list == null) return ItemUseResult.empty();
        for(InteractableComponent c : list){
            if(!c.isUsable(ctx)) continue;
            ItemUseResult result = c.onUse(ctx);
            if(!result.successful()) continue;
            return result;
        }
        return ItemUseResult.empty();
    }
}
