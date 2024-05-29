package killercreepr.cruxmenu.menu.bukkit.holder;

import killercreepr.crux.data.DataExchange;
import killercreepr.cruxmenu.menu.bukkit.ConfigMenu;
import killercreepr.cruxmenu.menu.bukkit.api.events.menu.MenuOpenEvent;
import killercreepr.cruxmenu.menu.bukkit.registry.MenuRegistry;
import killercreepr.crux.valueproviders.number.NumberProvider;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MenuHolder implements Keyed {
    protected MenuRegistry registry;
    public MenuHolder setRegistry(@NotNull MenuRegistry registry) {
        this.registry = registry; return this;
    }

    public MenuRegistry getRegistry() {
        return registry;
    }

    protected final @NotNull NamespacedKey key;
    protected final @Nullable String title;
    protected final @NotNull NumberProvider size;
    //priority => item
    protected final @NotNull MenuItems items;
    protected final @NotNull DataExchange info;

    public MenuHolder(@NotNull NamespacedKey key,
                      @Nullable String title,
                      @NotNull NumberProvider size,
                      @NotNull MenuItems items,
                      @NotNull DataExchange info) {
        this.key = key;
        this.title = title;
        this.size = size;
        this.items = items;
        this.info = info;
    }

    public @NotNull DataExchange info() {
        return info;
    }

    public @NotNull MenuOpenEvent open(@NotNull Player p){
        return open(p, DataExchange.empty());
    }

    public @NotNull MenuOpenEvent open(@NotNull Player p, @NotNull DataExchange data){
        DataExchange.Builder builder = new DataExchange.Builder().putAll(data);
        builder.put("viewer", p);

        ConfigMenu menu = new ConfigMenu(this, builder.build());
        menu.load();
        return menu.open(p);
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
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
}
