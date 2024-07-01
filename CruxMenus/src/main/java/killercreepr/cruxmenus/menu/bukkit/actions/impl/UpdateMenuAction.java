package killercreepr.cruxmenus.menu.bukkit.actions.impl;

import killercreepr.cruxmenus.menu.bukkit.actions.ActionContext;
import killercreepr.cruxmenus.menu.bukkit.actions.SimpleMenuAction;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UpdateMenuAction extends SimpleMenuAction {
    public UpdateMenuAction(@NotNull Key key) {
        super(key);
    }

    @Override
    public boolean execute(@NotNull Player p, @NotNull ActionContext actionInfo, @NotNull String[] args) {
        actionInfo.getMenu().getHolder().open(p, actionInfo.getMenu().info().append(actionInfo.getInfo()));
        return true;
    }
}
