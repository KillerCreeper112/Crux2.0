package killercreepr.cruxmenus.core.menu.action.impl;

import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.cruxmenus.api.menu.contex.ActionContext;
import killercreepr.cruxmenus.core.menu.action.SimpleMenuAction;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;

public class OpenBookAction extends SimpleMenuAction {
    public OpenBookAction(@NotNull Key key) {
        super(key);
    }

    @Override
    public boolean execute(@NotNull ActionContext ctx, @NotNull String[] args) {
        HumanEntity p = ctx.getPlayer();
        ItemStack book = DynamicItem.builder()
            .material(args[0]).build()
            .buildItem(TextParserContext.builder().tags(ctx.getAllMergedResolvers()).build());
        if(book == null) return false;
        if(!(book.getItemMeta() instanceof BookMeta meta)) return false;
        p.openBook(meta);
        return true;
    }
}
