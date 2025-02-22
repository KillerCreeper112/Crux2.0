package killercreepr.cruxmenus.core.menu.module.standard.fill.simple;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.api.menu.holder.MenuItemHolder;
import killercreepr.cruxmenus.api.menu.module.ActiveMenuModule;
import killercreepr.cruxmenus.api.menu.module.config.MenuModuleBuilder;
import killercreepr.cruxmenus.core.menu.module.SimpleMenuModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SimpleFillMenuModule extends SimpleMenuModule {
    protected final @NotNull String id;
    protected final @NotNull MenuModuleBuilder builder;
    protected final @NotNull NumberProvider indexes;
    protected final @NotNull MenuItemHolder menuItem;
    protected final @Nullable List<Integer> cachedIndexes;
    public SimpleFillMenuModule(@NotNull String id, @NotNull MenuModuleBuilder builder,
                                @NotNull NumberProvider indexes,
                                @NotNull MenuItemHolder menuItem, boolean cacheIndexes) {
        super(builder.key());
        this.id = id;
        this.builder = builder;
        this.indexes = indexes;
        this.menuItem = menuItem;
        if(cacheIndexes){
            cachedIndexes = new ArrayList<>();
            this.indexes.sampleList().forEach(i -> cachedIndexes.add(i.intValue()));
        }else cachedIndexes = null;
    }

    @Override
    public @Nullable ActiveMenuModule build(@NotNull Menu menu) {
        return new ActiveSimpleFillMenuModule(id, this, indexes, menuItem, cachedIndexes);
    }
}
