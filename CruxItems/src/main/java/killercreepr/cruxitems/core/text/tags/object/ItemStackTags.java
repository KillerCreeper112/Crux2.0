package killercreepr.cruxitems.core.text.tags.object;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringListResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.item.dynamic.component.DynamicItemLore;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.cruxitems.api.item.CruxedItem;
import killercreepr.cruxitems.api.item.plugin.DynamicPluginItem;
import killercreepr.cruxitems.core.component.CruxItemsComponents;
import killercreepr.cruxitems.core.registries.CruxItemRegistries;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemStackTags implements ObjectTag<ItemStack> {
    @Override
    public @NotNull Class<ItemStack> getObjectType() {
        return ItemStack.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("item_");
    }

    @Override
    public @Nullable TagContainer<StringListResolver> requestStringLists(@NotNull ItemStack object, @NotNull TagParser tags) {
        return TagContainer.stringList(tags)
            .add(Tag.stringList("plugin/lore", (args, ctx) ->{
                if(!(CruxedItem.cruxed(object).getPluginItem() instanceof DynamicPluginItem i)) return List.of();
                DynamicItem dynamic = i.dynamicItem();
                if(dynamic.components() == null) return List.of();
                if(!(dynamic.components().get("lore") instanceof DynamicItemLore lore)) return List.of();
                return lore.parseStringList(TextParserContext.empty());
            }))
            .add(Tag.stringList("plugin/lore_reference", (args, ctx) ->{
                Key key = CruxItem.wrap(object).get(CruxItemsComponents.PLUGIN_ITEM_REFERENCE);
                if(key == null) return List.of();
                if(!(CruxItemRegistries.ITEMS.get(key) instanceof DynamicPluginItem i)) return List.of();
                DynamicItem dynamic = i.dynamicItem();
                if(dynamic.components() == null) return List.of();
                if(!(dynamic.components().get("lore") instanceof DynamicItemLore lore)) return List.of();
                return lore.parseStringList(TextParserContext.empty());
            }))
            ;
    }
}
