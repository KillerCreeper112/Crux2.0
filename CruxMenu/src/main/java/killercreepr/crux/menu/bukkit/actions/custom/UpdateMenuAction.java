package killercreepr.crux.menu.bukkit.actions.custom;

import killercreepr.crux.menu.bukkit.actions.ActionContext;
import killercreepr.crux.menu.bukkit.actions.SimpleMenuAction;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UpdateMenuAction extends SimpleMenuAction {
    public UpdateMenuAction(@NotNull NamespacedKey key) {
        super(key);
    }

    @Override
    public boolean execute(@NotNull Player p, @NotNull ActionContext actionInfo, @NotNull String[] args) {
        actionInfo.getMenu().getHolder().open(p, actionInfo.getInfo());
        return true;
    }
}
