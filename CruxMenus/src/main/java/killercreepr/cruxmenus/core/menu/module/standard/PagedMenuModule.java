package killercreepr.cruxmenus.core.menu.module.standard;

import killercreepr.crux.Crux;
import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.Holder;
import killercreepr.crux.tags.resolver.Tag;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxmenus.api.menu.CfgMenu;
import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.api.menu.contex.MenuContext;
import killercreepr.cruxmenus.api.menu.holder.MenuItems;
import killercreepr.cruxmenus.api.menu.module.ActiveMenuModule;
import killercreepr.cruxmenus.api.menu.module.MenuModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class PagedMenuModule<T> implements MenuModule {
    protected final @NotNull String id;
    protected final @NotNull NumberProvider indexes;
    protected final @Nullable String valueFilter;
    protected final @Nullable MenuItems valueItems;
    protected final @Nullable MenuItems emptyItems;
    public PagedMenuModule(@NotNull String id, @NotNull NumberProvider indexes, @Nullable String valueFilter, @Nullable MenuItems valueItems, @Nullable MenuItems emptyItems) {
        this.id = id;
        this.indexes = indexes;
        this.valueFilter = valueFilter;
        this.valueItems = valueItems;
        this.emptyItems = emptyItems;
    }

    public abstract @NotNull Holder<List<T>> getValues(@NotNull Menu menu);

    @Override
    public @Nullable ActiveMenuModule build(@NotNull Menu menu) {
        return new ActivePagedMenuModule<>(id, this, indexes, valueFilter, getValues(menu)) {
            @Override
            public void setPagedItem(@NotNull Menu menu, int slot, int index, int listIndex, @NotNull T value) {
                if(valueItems == null) return;
                if(!(menu instanceof CfgMenu cfg)) return;
                MenuContext menuContext = MenuContext.context(cfg,
                    DataExchange.builder().putAll(cfg.info()).put(value).build(), cfg.buildTags().addAll(buildTags(
                    menu, cfg.getHolder().getRegistry().getFormat().tags()
                )).addAll(
                    Tag.parsed(MenuModule.buildTag(id, "slot"), slot+""),
                    Tag.parsed(MenuModule.buildTag(id, "index"), index+""),
                    Tag.parsed(MenuModule.buildTag(id, "list_index"), listIndex+"")
                ));

                cfg.setItems(valueItems, menuContext);
            }

            @Override
            public TextParserContext buildContext(@NotNull Menu menu) {
                if(!(menu instanceof CfgMenu cfg)) return super.buildContext(menu);
                return TextParserContext.builder()
                    .tags(
                        cfg.buildTags().addAll(buildTags(
                            menu, cfg.getHolder().getRegistry().getFormat().tags()
                        ))
                    ).build();
            }

            @Override
            public void setEmptyItem(@NotNull Menu menu, int slot, int index) {
                if(emptyItems == null) return;
                if(!(menu instanceof CfgMenu cfg)) return;
                MenuContext menuContext = MenuContext.context(cfg, cfg.info(), cfg.buildTags().addAll(
                    Tag.parsed(MenuModule.buildTag(id, "slot"), slot+""),
                    Tag.parsed(MenuModule.buildTag(id, "index"), index+"")
                ));
                cfg.setItems(emptyItems, menuContext);
            }
        };
    }
}
