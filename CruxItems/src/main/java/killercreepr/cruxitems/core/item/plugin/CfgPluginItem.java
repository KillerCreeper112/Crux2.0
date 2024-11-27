package killercreepr.cruxitems.core.item.plugin;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.cruxitems.api.event.CustomItemPreUseEvent;
import killercreepr.cruxitems.api.event.InteractableComponentUseEvent;
import killercreepr.cruxitems.api.item.CruxedItem;
import killercreepr.cruxitems.api.item.component.InteractableComponent;
import killercreepr.cruxitems.api.item.interaction.InteractableItem;
import killercreepr.cruxitems.api.item.interaction.ItemUseContext;
import killercreepr.cruxitems.api.item.interaction.ItemUseResult;
import killercreepr.cruxitems.api.item.plugin.DynamicPluginItem;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class CfgPluginItem extends GenericPluginItem implements DynamicPluginItem, InteractableItem {
    protected final @NotNull DynamicItem item;
    public CfgPluginItem(@NotNull Key key, @NotNull DynamicItem item) {
        super(key);
        this.item = item;
    }

    @Override
    public @NotNull DynamicItem dynamicItem() {
        return item;
    }

    @Override
    public @NotNull CruxedItem build(@Nullable Entity holder, @Nullable MergedTagContainer tags) {
        ItemStack built = item.buildItem(TextParserContext.builder().tags(tags).build());
        if(built == null) throw new IllegalStateException(key + " plugin item returned NULL!");
        return CruxedItem.cruxed(built);
    }

    @Override
    public @NotNull ItemUseResult onUse(@NotNull ItemUseContext ctx) {
        CustomItemPreUseEvent preUseEvent = new CustomItemPreUseEvent(ctx, ItemUseResult.empty());
        if(!preUseEvent.callEvent()) return preUseEvent.getUseResult();

        CruxItem item = ctx.getItem();
        Collection<InteractableComponent> list = item.getAllOfType(InteractableComponent.class);
        if(list == null) return ItemUseResult.empty();
        ItemUseResult defaultResult = preUseEvent.getUseResult();
        for(InteractableComponent c : list){
            if(!c.isUsable(ctx)) continue;
            InteractableComponentUseEvent useEvent = new InteractableComponentUseEvent(ctx, c);
            if(!useEvent.callEvent()) continue;

            ItemUseResult result = c.onUse(ctx);
            if(!result.successful()) continue;
            return result;
        }
        return defaultResult;
    }
}
