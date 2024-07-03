package killercreepr.cruxmenus.menu.bukkit.module.standard;

import killercreepr.crux.data.NotNullHolder;
import killercreepr.crux.item.dynamic.DynamicItem;
import killercreepr.crux.tags.container.MultiTagContainer;
import killercreepr.crux.tags.context.FormatParserContext;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxmenus.menu.bukkit.CfgMenu;
import killercreepr.cruxmenus.menu.bukkit.Menu;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuHolder;
import killercreepr.cruxmenus.menu.bukkit.module.ActiveMenuModule;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class PagedCfgModule<T> extends PagedMenuModule<T> {
    protected final @Nullable DynamicItem valueItem;
    protected final @Nullable DynamicItem emptyItem;
    public PagedCfgModule(@NotNull Key key, @NotNull String id, @NotNull NumberProvider indexes,
                          @Nullable DynamicItem valueItem, @Nullable DynamicItem emptyItem) {
        super(key, id, indexes);
        this.valueItem = valueItem;
        this.emptyItem = emptyItem;
    }

    public abstract @NotNull NotNullHolder<List<T>> getValues(@NotNull CfgMenu menu);

    @Override
    public @Nullable ActiveMenuModule build(@NotNull Menu menu, @NotNull String id, @NotNull List<Integer> list) {
        if(!(menu instanceof CfgMenu cfg)) return null;
        return new ActivePagedMenuModule<T>(id, this, parseIndexes(menu), getValues(cfg)) {
            @Override
            public @Nullable ItemStack buildPagedItem(@NotNull Menu menu, @NotNull T member) {
                if(valueItem == null) return null;
                if(!(menu instanceof CfgMenu cfg)) return null;
                MenuHolder holder = cfg.getHolder();
                return valueItem.buildItem(
                    new FormatParserContext.Builder(holder.getRegistry().getFormat())
                        .tags(
                            new MultiTagContainer(holder.getRegistry().getFormat().tags())
                                .addAll(cfg.buildTags())
                                .hook(member)
                        ).build()
                );
            }

            @Override
            public @Nullable ItemStack buildEmptyItem(@NotNull Menu menu) {
                if(emptyItem == null) return null;
                if(!(menu instanceof CfgMenu cfg)) return null;
                MenuHolder holder = cfg.getHolder();
                return emptyItem.buildItem(
                    new FormatParserContext.Builder(holder.getRegistry().getFormat())
                        .tags(
                            new MultiTagContainer(holder.getRegistry().getFormat().tags())
                                .addAll(cfg.buildTags())
                        ).build()
                );
            }
        };
    }
}
