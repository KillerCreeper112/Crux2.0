package killercreepr.crux.menu.bukkit;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.tags.container.ObjectStringHookContainer;
import org.jetbrains.annotations.NotNull;

public class MenuInfo {
    private final @NotNull ConfigMenu menu;
    private final @NotNull DataExchange info;
    private final @NotNull ObjectStringHookContainer resolvers;

    public MenuInfo(@NotNull ConfigMenu menu, @NotNull DataExchange info, @NotNull ObjectStringHookContainer resolvers) {
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
