package killercreepr.crux.menu.bukkit.actions.custom;

import killercreepr.crux.Crux;
import killercreepr.crux.menu.bukkit.actions.ActionInfo;
import killercreepr.crux.menu.bukkit.actions.SimpleMenuAction;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MessageAction extends SimpleMenuAction {

    public MessageAction(@NotNull NamespacedKey key) {
        super(key);
    }

    @Override
    public boolean execute(@NotNull Player p, @NotNull ActionInfo actionInfo, @NotNull String[] args) {
        p.sendMessage(Crux.FORMAT.deserialize(args[0]));
        return true;
    }
}
