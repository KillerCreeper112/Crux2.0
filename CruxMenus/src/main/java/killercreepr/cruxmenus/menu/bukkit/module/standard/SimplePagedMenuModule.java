package killercreepr.cruxmenus.menu.bukkit.module.standard;

import killercreepr.crux.data.NotNullHolder;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxmenus.menu.bukkit.CfgMenu;
import killercreepr.cruxmenus.menu.bukkit.Menu;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuItemHolder;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuItems;
import killercreepr.cruxmenus.menu.bukkit.module.config.MenuModuleBuilder;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SimplePagedMenuModule<T> extends PagedMenuModule<T>{
    protected final @NotNull MenuModuleBuilder builder;
    public SimplePagedMenuModule(@NotNull String id, @NotNull NumberProvider indexes, @Nullable MenuItems valueItems,
                                 @Nullable MenuItems emptyItems, @NotNull MenuModuleBuilder builder) {
        super(id, indexes, valueItems, emptyItems);
        this.builder = builder;
    }

    @Override
    public @NotNull NotNullHolder<List<T>> getValues(@NotNull Menu menu) {
        if(!(menu instanceof CfgMenu c)) throw new RuntimeException();
        return getValues(c);
    }

    public abstract @NotNull NotNullHolder<List<T>> getValues(@NotNull CfgMenu menu);

    @Override
    public @NotNull Key key() {
        return builder.key();
    }
}
