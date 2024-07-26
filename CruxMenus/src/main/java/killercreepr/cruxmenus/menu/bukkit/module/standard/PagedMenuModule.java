package killercreepr.cruxmenus.menu.bukkit.module.standard;

import killercreepr.crux.context.DummyInputContext;
import killercreepr.crux.context.InputContext;
import killercreepr.crux.context.SimpleInputContext;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.NotNullHolder;
import killercreepr.crux.tags.resolver.Tag;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.crux.valueproviders.number.UniformNumber;
import killercreepr.crux.valueproviders.number.UniformNumberArray;
import killercreepr.cruxmenus.menu.bukkit.CfgMenu;
import killercreepr.cruxmenus.menu.bukkit.Menu;
import killercreepr.cruxmenus.menu.bukkit.MenuContext;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuItemHolder;
import killercreepr.cruxmenus.menu.bukkit.module.ActiveMenuModule;
import killercreepr.cruxmenus.menu.bukkit.module.MenuModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class PagedMenuModule<T> implements MenuModule {
    protected final @NotNull String id;
    protected final @NotNull NumberProvider indexes;
    protected final @Nullable MenuItemHolder valueItem;
    protected final @Nullable MenuItemHolder emptyItem;
    public PagedMenuModule(@NotNull String id, @NotNull NumberProvider indexes, @Nullable MenuItemHolder valueItem, @Nullable MenuItemHolder emptyItem) {
        this.id = id;
        this.indexes = indexes;
        this.valueItem = valueItem;
        this.emptyItem = emptyItem;
    }

    public abstract @NotNull NotNullHolder<List<T>> getValues(@NotNull Menu menu);

    public @NotNull List<Integer> parseIndexes(@NotNull Menu menu){
        List<Integer> list = new ArrayList<>();
        InputContext ctx;
        if(menu instanceof CfgMenu cfg){
            ctx = new SimpleInputContext(cfg.getHolder().getRegistry().getFormat(), cfg.buildTags());
        }else ctx = new DummyInputContext();

        if(indexes instanceof UniformNumberArray array){
            for(NumberProvider p : array.getNumbers()){
                list.add(p.sample(ctx).intValue());
            }
            return list;
        }
        if(indexes instanceof UniformNumber random){
            for(int i = random.getMinInclusive().sample(ctx).intValue(); i <= random.getMaxInclusive().sample(ctx).intValue(); i++){
                list.add(i);
            }
            return list;
        }
        list.add(indexes.sample(ctx).intValue());
        return list;
    }

    @Override
    public @Nullable ActiveMenuModule build(@NotNull Menu menu) {
        return new ActivePagedMenuModule<T>(id, this, parseIndexes(menu), getValues(menu)) {
            @Override
            public void setPagedItem(@NotNull Menu menu, int slot, @NotNull T value) {
                if(valueItem == null) return;
                if(!(menu instanceof CfgMenu cfg)) return;
                MenuContext menuContext = new MenuContext(cfg,
                    DataExchange.builder().putAll(cfg.info()).put(value).build(), cfg.buildTags().addAll(buildTags(
                    menu, cfg.getHolder().getRegistry().getFormat().tags()
                )).add(
                    Tag.parsed(MenuModule.buildTag(id, "slot"), slot+"")
                ));



                cfg.setItem(valueItem, menuContext);
            }

            @Override
            public void setEmptyItem(@NotNull Menu menu, int slot) {
                if(emptyItem == null) return;
                if(!(menu instanceof CfgMenu cfg)) return;
                MenuContext menuContext = new MenuContext(cfg, cfg.info(), cfg.buildTags().add(
                    Tag.parsed(MenuModule.buildTag(id, "slot"), slot+"")
                ));
                cfg.setItem(emptyItem, menuContext);
            }
        };
    }
}
