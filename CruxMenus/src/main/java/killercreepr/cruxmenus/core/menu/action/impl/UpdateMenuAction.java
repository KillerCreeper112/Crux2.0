package killercreepr.cruxmenus.core.menu.action.impl;

import killercreepr.cruxmenus.api.menu.contex.ActionContext;
import killercreepr.cruxmenus.core.menu.action.SimpleMenuAction;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class UpdateMenuAction extends SimpleMenuAction {
    public UpdateMenuAction(@NotNull Key key) {
        super(key);
    }

    @Override
    public boolean execute(@NotNull ActionContext ctx, @NotNull String[] args) {
        ctx.getMenu().refresh();
        return true;
    }
}
