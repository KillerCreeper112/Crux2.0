package killercreepr.cruxmenus.core.menu.module.standard;

import killercreepr.crux.data.Holder;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxmenus.api.menu.CfgMenu;
import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.api.menu.holder.MenuItems;
import killercreepr.cruxmenus.api.menu.module.config.MenuModuleBuilder;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SimplePagedMenuModule<T> extends PagedMenuModule<T>{
    protected final @NotNull MenuModuleBuilder builder;
    public SimplePagedMenuModule(@NotNull String id, @NotNull NumberProvider indexes, @Nullable String valueFilter,
                                 @Nullable MenuItems valueItems,
                                 @Nullable MenuItems emptyItems, @NotNull MenuModuleBuilder builder) {
        super(id, indexes, valueFilter, valueItems, emptyItems);
        this.builder = builder;
    }

    @Override
    public @NotNull Holder<List<T>> getValues(@NotNull Menu menu) {
        if(!(menu instanceof CfgMenu c)) throw new RuntimeException();
        return getValues(c);
    }

    public abstract @NotNull Holder<List<T>> getValues(@NotNull CfgMenu menu);

    @Override
    public @NotNull Key key() {
        return builder.key();
    }
}
