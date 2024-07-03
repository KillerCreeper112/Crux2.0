package killercreepr.cruxmenus.menu.bukkit.module.standard;

import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxmenus.menu.bukkit.config.handlers.FileMenuHolder;
import killercreepr.cruxmenus.menu.bukkit.config.handlers.FileMenuModuled;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuItemHolder;
import killercreepr.cruxmenus.menu.bukkit.module.MenuModule;
import killercreepr.cruxmenus.menu.bukkit.module.config.MenuModuleBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class FilePagedCfg extends FileMenuModuled<MenuModule> implements MenuModuleBuilder {
    public FilePagedCfg(@NotNull FileMenuHolder menuModule) {
        super(menuModule);
    }

    public @Nullable MenuItemHolder parseValueItem(@NotNull FileContext<?> ctx,
                                                   @NotNull FileElement e, @Nullable FileObject menuContext){
        if(!(e instanceof FileObject o)) return null;
        return menuModule.getYamlMenuItem().deserializeFromFile(ctx, o.get("value_item"), menuContext);
    }

    public @Nullable MenuItemHolder parseEmptyItem(@NotNull FileContext<?> ctx,
                                                   @NotNull FileElement e, @Nullable FileObject menuContext){
        if(!(e instanceof FileObject o)) return null;
        return menuModule.getYamlMenuItem().deserializeFromFile(ctx, o.get("empty_item"), menuContext);
    }

    @Override
    public @Nullable MenuModule deserializeFromFile(@NotNull FileContext<?> ctx,
                                                                  @NotNull FileElement e, @Nullable FileObject menuContext) {
        if(!(e instanceof FileObject o)) return null;

        String id = o.getObject(String.class, "id");
        if(id==null) return null;

        NumberProvider indexes = parsePageIndexes(ctx, e, menuContext);
        if(indexes==null) return null;

        MenuItemHolder valueItem = parseValueItem(ctx, e, menuContext);
        MenuItemHolder emptyItem = parseEmptyItem(ctx, e, menuContext);


        return parsePaged(ctx, o, menuContext, id, indexes, valueItem, emptyItem);
    }

    public abstract @Nullable MenuModule parsePaged(@NotNull FileContext<?> ctx,
                                           @NotNull FileObject o,
                                           @Nullable FileObject menuContext,
                                           @NotNull String id,
                                           @NotNull NumberProvider indexes,
                                           @Nullable MenuItemHolder valueItem,
                                           @Nullable MenuItemHolder emptyItem);

    public @Nullable NumberProvider parsePageIndexes(@NotNull FileContext<?> ctx,
                                                     @NotNull FileElement e,
                                                     @Nullable FileObject menuContext){
        if(!(e instanceof FileObject o)) return null;
        return ctx.getRegistry().deserialize(NumberProvider.class, o.get("indexes"), ctx);
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
