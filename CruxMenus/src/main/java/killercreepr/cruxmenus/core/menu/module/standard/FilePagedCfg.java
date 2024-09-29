package killercreepr.cruxmenus.core.menu.module.standard;

import killercreepr.crux.data.Holder;
import killercreepr.crux.valueproviders.number.EquationNumber;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxmenus.api.menu.config.handler.FileMenuHolder;
import killercreepr.cruxmenus.api.menu.holder.MenuItemHolder;
import killercreepr.cruxmenus.api.menu.holder.MenuItems;
import killercreepr.cruxmenus.api.menu.module.MenuModule;
import killercreepr.cruxmenus.api.menu.module.config.MenuModuleBuilder;
import killercreepr.cruxmenus.core.menu.config.handlers.SimpleFileMenuModuled;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class FilePagedCfg extends SimpleFileMenuModuled<MenuModule> implements MenuModuleBuilder {
    public FilePagedCfg(@NotNull FileMenuHolder<?> menuModule) {
        super(menuModule);
    }

    public @Nullable MenuItems parseValueItems(@NotNull FileContext<?> ctx,
                                                     @NotNull FileElement e, @Nullable FileObject menuContext,
                                                     @NotNull String id){
        if(!(e instanceof FileObject o)) return null;
        return menuModule.getFileMenuItems().deserializeFromFile(ctx, o.get("value_items"), menuContext,
            itemsFunction(id));
    }

    public @Nullable MenuItems parseEmptyItems(@NotNull FileContext<?> ctx,
                                               @NotNull FileElement e, @Nullable FileObject menuContext,
                                               @NotNull String id){
        if(!(e instanceof FileObject o)) return null;
        return menuModule.getFileMenuItems().deserializeFromFile(ctx, o.get("empty_items"), menuContext,
            itemsFunction(id));
    }

    public @Nullable Function<MenuItemHolder, MenuItemHolder> itemsFunction(@NotNull String id){
        return item ->{
            if(item.info().has("slot")) return item;
            return MenuItemHolder.holder(
                item.getItem(),
                item.info().append("slot", Holder.directObject(new EquationNumber("<"+MenuModule.buildTag(id, "slot") + ">"))),
                item.getClickActions()
            );
        };
    }

    @Override
    public @Nullable MenuModule deserializeFromFile(@NotNull FileContext<?> ctx,
                                                                  @NotNull FileElement e, @Nullable FileObject menuContext) {
        if(!(e instanceof FileObject o)) return null;

        String id = o.getObject(String.class, "id");
        if(id==null) return null;

        NumberProvider indexes = parsePageIndexes(ctx, e, menuContext);
        if(indexes==null) return null;

        String valuesFilter = o.getObject(String.class, "filter");

        MenuItems valueItems = parseValueItems(ctx, e, menuContext, id);
        MenuItems emptyItems = parseEmptyItems(ctx, e, menuContext, id);

        return parsePaged(ctx, o, menuContext, id, indexes, valuesFilter, valueItems, emptyItems);
    }

    public abstract @Nullable MenuModule parsePaged(@NotNull FileContext<?> ctx,
                                                    @NotNull FileObject o,
                                                    @Nullable FileObject menuContext,
                                                    @NotNull String id,
                                                    @NotNull NumberProvider indexes,
                                                    @Nullable String valuesFilter,
                                                    @Nullable MenuItems valueItems,
                                                    @Nullable MenuItems emptyItems);

    public @Nullable NumberProvider parsePageIndexes(@NotNull FileContext<?> ctx,
                                                     @NotNull FileElement e,
                                                     @Nullable FileObject menuContext){
        if(!(e instanceof FileObject o)) return null;
        return ctx.getRegistry().deserializeFromFile(NumberProvider.class, o.get("indexes"), ctx);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "menu_paged_cfg_module";
    }

    @Override
    public @Nullable MenuModule build(@NotNull FileContext<?> context, @NotNull FileElement e, @Nullable FileObject menuContext) {
        return deserializeFromFile(context, e, menuContext);
    }
}
