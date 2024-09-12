package killercreepr.cruxmenus.menu.bukkit.holder;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.DataInfoHolder;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxmenus.menu.bukkit.CfgMenu;
import killercreepr.cruxmenus.menu.bukkit.ConfigMenu;
import killercreepr.cruxmenus.menu.bukkit.api.events.menu.MenuOpenEvent;
import killercreepr.cruxmenus.menu.bukkit.module.MenuModule;
import killercreepr.cruxmenus.menu.bukkit.registry.MenuRegistry;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class MenuHolder implements Keyed, DataInfoHolder {
    protected MenuRegistry registry;
    public MenuHolder setRegistry(@NotNull MenuRegistry registry) {
        this.registry = registry; return this;
    }

    public MenuRegistry getRegistry() {
        return registry;
    }

    protected final @NotNull Key key;
    protected final @Nullable String title;
    protected final @NotNull NumberProvider size;
    //priority => item
    protected final @NotNull MenuItems items;
    protected final @NotNull DataExchange info;
    protected final @NotNull Collection<MenuModule> modules;

    public MenuHolder(@NotNull Key key,
                      @Nullable String title,
                      @NotNull NumberProvider size,
                      @NotNull MenuItems items,
                      @NotNull DataExchange info,
                      @NotNull Collection<MenuModule> modules) {
        this.key = key;
        this.title = title;
        this.size = size;
        this.items = items;
        this.info = info;
        this.modules = modules;
    }

    @Override
    public @NotNull DataExchange info() {
        return info;
    }

    public @NotNull MenuOpenEvent open(@NotNull Player p){
        return open(p, DataExchange.empty());
    }

    public @NotNull MenuOpenEvent open(@NotNull Player p, @NotNull DataExchange data){
        DataExchange.Builder builder = DataExchange.builder().putAll(data);
        builder.put("viewer", p);

        CfgMenu menu = createMenu(builder.build());
        menu.load();
        return menu.open(p);
    }

    public @NotNull CfgMenu createMenu(@NotNull DataExchange data){
        return new ConfigMenu(this, data);
    }

    public @Nullable String getTitle() {
        return title;
    }

    public @NotNull NumberProvider getSize() {
        return size;
    }

    public @NotNull MenuItems getItems() {
        return items;
    }

    public @NotNull Collection<MenuModule> getModules() {
        return modules;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
