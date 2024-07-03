package killercreepr.cruxmenus.menu.bukkit.module.standard;

import killercreepr.crux.item.dynamic.DynamicItem;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxmenus.menu.bukkit.module.MenuModule;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PagedCfgModuleBuilder<T> extends PagedMenuModuleBuilder {
    protected final @NotNull Key key;
    public PagedCfgModuleBuilder(@NotNull Key key) {
        this.key = key;
    }

    @Nullable
    @Override
    public MenuModule build(@NotNull String id, @NotNull FileContext<?> ctx,
                            @NotNull FileObject o, @Nullable FileObject menuBase) {
        NumberProvider indexes = parsePageIndexes(ctx, o, menuBase);
        if(indexes==null) return null;
        DynamicItem valueItem = ctx.getRegistry().deserialize(DynamicItem.class, o.get("value_item"));
        DynamicItem emptyItem = ctx.getRegistry().deserialize(DynamicItem.class, o.get("empty_item"));
        return buildModule(id, ctx, o, menuBase, valueItem, emptyItem);
    }

    public abstract @NotNull PagedCfgModule<T> buildModule(@NotNull String id, @NotNull FileContext<?> ctx,
                                                           @NotNull FileObject o, @Nullable FileObject menuBase,
                                                           @Nullable DynamicItem valueItem, @Nullable DynamicItem emptyItem);

    @Override
    public @NotNull Key key() {
        return key;
    }
}
