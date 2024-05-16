package killercreepr.crux.menu.bukkit.holder;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.Holder;
import killercreepr.crux.menu.bukkit.ConfigMenu;
import killercreepr.crux.menu.bukkit.registry.MenuRegistry;
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
    protected final @NotNull String size;
    //priority => item
    protected final @NotNull MenuItems items;
    protected final @NotNull DataExchange info;

    public MenuHolder(@NotNull NamespacedKey key,
                      @Nullable String title,
                      @NotNull String size,
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

    public @NotNull ConfigMenu open(@NotNull Player p){
        return open(p, DataExchange.empty());
    }

    public @NotNull ConfigMenu open(@NotNull Player p, @NotNull DataExchange data){
        DataExchange.Builder builder = new DataExchange.Builder().putAll(data);
        builder.put("viewer", Holder.direct(p));
        ConfigMenu menu = new ConfigMenu(this, builder.build());
        return (ConfigMenu) menu.setItems(this).open(p);
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }

    public @Nullable String getTitle() {
        return title;
    }

    public @NotNull String getSize() {
        return size;
    }

    public @NotNull MenuItems getItems() {
        return items;
    }
}
