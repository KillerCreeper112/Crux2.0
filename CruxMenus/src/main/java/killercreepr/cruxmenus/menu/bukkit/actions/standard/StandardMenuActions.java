package killercreepr.cruxmenus.menu.bukkit.actions.standard;

import killercreepr.crux.Crux;
import killercreepr.cruxmenus.menu.bukkit.actions.MenuAction;
import killercreepr.cruxmenus.menu.bukkit.actions.impl.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;

public class StandardMenuActions {
    public static @NotNull Collection<MenuAction> buildActions(){
        return new HashSet<>(){{
            add(new CloseInventoryAction(Crux.key("close")));
            add(new CommandAction(Crux.key("player_command")));
            add(new ConsoleCommandAction(Crux.key("console_command")));
            add(new MessageAction(Crux.key("message")));
            add(new OpenMenuAction(Crux.key("open")));
            add(new SoundAction(Crux.key("sound")));
            add(new UpdateMenuAction(Crux.key("update")));
            add(new UpdateItemAction(Crux.key("update_item")));

            add(new PagedAddAction(Crux.key("page_add")));
            add(new PagedSetAction(Crux.key("page_set")));
        }};
    }
}
