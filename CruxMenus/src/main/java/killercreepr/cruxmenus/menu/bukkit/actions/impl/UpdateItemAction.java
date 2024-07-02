package killercreepr.cruxmenus.menu.bukkit.actions.impl;

import killercreepr.crux.util.CruxMath;
import killercreepr.cruxmenus.menu.bukkit.CfgMenu;
import killercreepr.cruxmenus.menu.bukkit.actions.ActionContext;
import killercreepr.cruxmenus.menu.bukkit.actions.SimpleMenuAction;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class UpdateItemAction extends SimpleMenuAction {
    public UpdateItemAction(@NotNull Key key) {
        super(key);
    }

    @Override
    public boolean execute(@NotNull Player p, @NotNull ActionContext ctx, @NotNull String[] args) {
        InventoryClickEvent event = ctx.getEvent();
        CfgMenu menu = ctx.getMenu();

        int slot = args.length > 0 ? (int)CruxMath.evaluate(args[0]) : event.getSlot();
        return menu.setItem(menu.getHolder(), slot) != null;
    }
}
