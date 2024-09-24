package killercreepr.cruxmenus.core.menu.action.impl;

import killercreepr.crux.util.CruxString;
import killercreepr.cruxmenus.api.menu.contex.ActionContext;
import killercreepr.cruxmenus.core.menu.action.SimpleMenuAction;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MessageAction extends SimpleMenuAction {
    public MessageAction(@NotNull Key key) {
        super(key);
    }

    @Override
    public boolean has(@NotNull String x) {
        return super.has(x) || x.equalsIgnoreCase("msg");
    }

    @Override
    public boolean execute(@NotNull Player p, @NotNull ActionContext actionInfo, @NotNull String[] args) {
        p.sendMessage(actionInfo.getItem().getFormat().deserialize(
            CruxString.join(args)
        ));
        return true;
    }
}
