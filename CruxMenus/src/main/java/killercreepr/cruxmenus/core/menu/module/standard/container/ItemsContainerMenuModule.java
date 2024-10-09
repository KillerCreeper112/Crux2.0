package killercreepr.cruxmenus.core.menu.module.standard.container;

import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.api.menu.holder.MenuItemHolder;
import killercreepr.cruxmenus.api.menu.holder.MenuItems;
import killercreepr.cruxmenus.api.menu.module.ActiveMenuModule;
import killercreepr.cruxmenus.api.menu.module.config.MenuModuleBuilder;
import killercreepr.cruxmenus.core.menu.module.SimpleMenuModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemsContainerMenuModule extends SimpleMenuModule {
    protected final @NotNull String id;
    protected final @NotNull MenuModuleBuilder builder;
    protected final @Nullable MenuItemHolder defaultBaseItem;
    protected final @NotNull MenuItems items;

    public ItemsContainerMenuModule(@NotNull String id, @NotNull MenuModuleBuilder builder,
                                    @Nullable MenuItemHolder defaultBaseItem, @NotNull MenuItems items) {
        super(builder.key());
        this.id = id;
        this.builder = builder;
        this.defaultBaseItem = defaultBaseItem;
        this.items = items;
    }

    @Override
    public @Nullable ActiveMenuModule build(@NotNull Menu menu) {
        return new ActiveItemsContainerMenuModule(id, this, items);
    }
}
