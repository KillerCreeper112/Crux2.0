package killercreepr.crux.menu.actions.custom;

import killerceepr.crux.menu.actions.ActionInfo;
import killerceepr.crux.menu.actions.SimpleMenuAction;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UpdateMenuAction extends SimpleMenuAction {
    public UpdateMenuAction(@NotNull NamespacedKey key) {
        super(key);
    }

    @Override
    public boolean execute(@NotNull Player p, @NotNull ActionInfo actionInfo, @NotNull String[] args) {
        actionInfo.getMenu().getHolder().open(p, actionInfo.getInfo());
        return true;
    }
}
