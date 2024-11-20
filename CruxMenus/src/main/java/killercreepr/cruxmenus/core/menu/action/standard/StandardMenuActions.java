package killercreepr.cruxmenus.core.menu.action.standard;

import killercreepr.crux.core.Crux;
import killercreepr.cruxmenus.api.menu.action.MenuAction;
import killercreepr.cruxmenus.core.menu.action.impl.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

public class StandardMenuActions {
    public static @NotNull Collection<MenuAction> buildActions(){
        return Set.of(
            new CloseInventoryAction(Crux.key("close")),
            new CommandAction(Crux.key("player_command")),
            new ConsoleCommandAction(Crux.key("console_command")),
            new MessageAction(Crux.key("message")),
            new OpenMenuAction(Crux.key("open")),
            new SoundAction(Crux.key("sound")),
            new UpdateMenuAction(Crux.key("update")),
            new UpdateItemAction(Crux.key("update_item")),

            new PagedAddAction(Crux.key("page_add")),
            new PagedSetAction(Crux.key("page_set"))
        );
    }
}
