package killercreepr.cruxmenus.api.menu.holder;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.data.holder.DataInfoHolder;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxmenus.api.event.MenuOpenEvent;
import killercreepr.cruxmenus.api.menu.CfgMenu;
import killercreepr.cruxmenus.api.menu.module.MenuModule;
import killercreepr.cruxmenus.api.menu.registry.MenuRegistry;
import killercreepr.cruxmenus.core.menu.holder.SimpleMenuHolder;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface MenuHolder extends DataInfoHolder, Keyed {
    static MenuHolder holder(@NotNull Key key,
                             @Nullable String title,
                             @NotNull NumberProvider size,
                             @NotNull MenuItems items,
                             @NotNull DataExchange info,
                             @NotNull Collection<MenuModule> modules){
        return new SimpleMenuHolder(key, title, size, items, info, modules);
    }

    MenuHolder setRegistry(@NotNull MenuRegistry registry);
    MenuRegistry getRegistry();

    default @NotNull MenuOpenEvent open(@NotNull HumanEntity p){
        return open(p, DataExchange.empty());
    }

    default @NotNull MenuOpenEvent open(@NotNull HumanEntity p, @NotNull DataExchange data){
        return open(p, data, null);
    };
    @NotNull MenuOpenEvent open(@NotNull HumanEntity p, @NotNull DataExchange data, @Nullable MergedTagContainer tags);

    @NotNull CfgMenu createMenu(@NotNull DataExchange data);

    @Nullable String getTitle();

    @NotNull NumberProvider getSize();

    @NotNull
    MenuItems getItems();

    @NotNull Collection<MenuModule> getModules();

    @Override
    @NotNull Key key();
}
