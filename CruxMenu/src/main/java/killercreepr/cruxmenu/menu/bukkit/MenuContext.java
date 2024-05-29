package killercreepr.cruxmenu.menu.bukkit;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.tags.container.ObjectStringHookContainer;
import org.jetbrains.annotations.NotNull;

public class MenuContext {
    protected final @NotNull ConfigMenu menu;
    protected final @NotNull DataExchange info;
    protected final @NotNull ObjectStringHookContainer resolvers;

    public MenuContext(@NotNull ConfigMenu menu, @NotNull DataExchange info, @NotNull ObjectStringHookContainer resolvers) {
        this.menu = menu;
        this.info = info;
        this.resolvers = resolvers;
    }

    public @NotNull ConfigMenu getMenu() {
        return menu;
    }

    public @NotNull DataExchange getInfo() {
        return info;
    }

    public @NotNull ObjectStringHookContainer getResolvers() {
        return resolvers;
    }
}
