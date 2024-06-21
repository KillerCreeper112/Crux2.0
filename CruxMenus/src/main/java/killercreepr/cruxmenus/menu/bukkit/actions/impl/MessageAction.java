package killercreepr.cruxmenus.menu.bukkit.actions.impl;

import killercreepr.cruxmenus.menu.bukkit.actions.ActionContext;
import killercreepr.cruxmenus.menu.bukkit.actions.SimpleMenuAction;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MessageAction extends SimpleMenuAction {

    public MessageAction(@NotNull Key key) {
        super(key);
    }

    @Override
    public boolean execute(@NotNull Player p, @NotNull ActionContext actionInfo, @NotNull String[] args) {
        p.sendMessage(actionInfo.getItem().getFormat().deserialize(args[0]));
        return true;
    }
}
