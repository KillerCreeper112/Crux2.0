package killercreepr.cruxmenus.core.menu.holder;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxmenus.api.event.MenuOpenEvent;
import killercreepr.cruxmenus.api.menu.CfgMenu;
import killercreepr.cruxmenus.api.menu.holder.MenuHolder;
import killercreepr.cruxmenus.api.menu.holder.MenuItems;
import killercreepr.cruxmenus.api.menu.module.MenuModule;
import killercreepr.cruxmenus.api.menu.registry.MenuRegistry;
import killercreepr.cruxmenus.core.menu.ConfigMenu;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class SimpleMenuHolder implements MenuHolder {
    protected MenuRegistry registry;
    public SimpleMenuHolder setRegistry(@NotNull MenuRegistry registry) {
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

    public SimpleMenuHolder(@NotNull Key key,
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

    @Override
    public @NotNull MenuOpenEvent open(@NotNull HumanEntity p, @NotNull DataExchange data, @Nullable MergedTagContainer tags) {
        DataExchange.Builder builder = DataExchange.builder().putAll(data);
        builder.put("viewer", p);

        CfgMenu menu = createMenu(builder.build(), tags);
        menu.load();
        return menu.open(p);
    }

    public @NotNull CfgMenu createMenu(@NotNull DataExchange data){
        return new ConfigMenu(this, data);
    }

    public @NotNull CfgMenu createMenu(@NotNull DataExchange data, @Nullable MergedTagContainer tags){
        return new ConfigMenu(this, data, tags);
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
