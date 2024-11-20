package killercreepr.cruxmenus.core.menu.module.standard.fill;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.api.menu.holder.MenuItems;
import killercreepr.cruxmenus.api.menu.module.ActiveMenuModule;
import killercreepr.cruxmenus.api.menu.module.config.MenuModuleBuilder;
import killercreepr.cruxmenus.core.menu.module.SimpleMenuModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FillMenuModule extends SimpleMenuModule {
    protected final @NotNull String id;
    protected final @NotNull MenuModuleBuilder builder;
    protected final @NotNull NumberProvider indexes;
    protected final @NotNull MenuItems menuItems;
    public FillMenuModule(@NotNull String id, @NotNull MenuModuleBuilder builder, @NotNull NumberProvider indexes, @NotNull MenuItems menuItems) {
        super(builder.key());
        this.id = id;
        this.builder = builder;
        this.indexes = indexes;
        this.menuItems = menuItems;
    }

    @Override
    public @Nullable ActiveMenuModule build(@NotNull Menu menu) {
        return new ActiveFillMenuModule(id, this, indexes, menuItems);
    }
}
