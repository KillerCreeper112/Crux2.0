package killercreepr.cruxmenus.menu.bukkit.module.standard;

import killercreepr.crux.data.NotNullHolder;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxmenus.menu.bukkit.CfgMenu;
import killercreepr.cruxmenus.menu.bukkit.Menu;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuItemHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SimplePagedMenuModule<T> extends PagedMenuModule<T>{
    public SimplePagedMenuModule(@NotNull String id, @NotNull NumberProvider indexes, @Nullable MenuItemHolder valueItem,
                                 @Nullable MenuItemHolder emptyItem) {
        super(id, indexes, valueItem, emptyItem);
    }

    @Override
    public @NotNull NotNullHolder<List<T>> getValues(@NotNull Menu menu) {
        if(!(menu instanceof CfgMenu c)) throw new RuntimeException();
        return getValues(c);
    }

    public abstract @NotNull NotNullHolder<List<T>> getValues(@NotNull CfgMenu menu);
}
