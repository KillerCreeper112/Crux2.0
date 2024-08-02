package killercreepr.cruxmenus.menu.bukkit.actions.standard;

import killercreepr.crux.Crux;
import killercreepr.cruxmenus.menu.bukkit.actions.MenuAction;
import killercreepr.cruxmenus.menu.bukkit.actions.impl.*;
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
