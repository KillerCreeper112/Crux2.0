package killercreepr.cruxmenus.core.menu.module.standard.fill;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.valueproviders.number.EquationNumber;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxmenus.api.menu.config.handler.FileMenuHolder;
import killercreepr.cruxmenus.api.menu.holder.MenuItemHolder;
import killercreepr.cruxmenus.api.menu.holder.MenuItems;
import killercreepr.cruxmenus.api.menu.module.MenuModule;
import killercreepr.cruxmenus.api.menu.module.config.MenuModuleBuilder;
import killercreepr.cruxmenus.core.menu.config.handlers.SimpleFileMenuModuled;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class FillMenuModuleBuilder extends SimpleFileMenuModuled<MenuModule> implements MenuModuleBuilder {
    protected final @NotNull Key key;
    public FillMenuModuleBuilder(@NotNull FileMenuHolder<?> menuModule, @NotNull Key key) {
        super(menuModule);
        this.key = key;
    }

    @Override
    public @Nullable MenuModule build(@NotNull FileContext<?> ctx, @NotNull FileElement e, @Nullable FileObject menuContext) {
        return deserializeFromFile(ctx, e, menuContext);
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

    public @Nullable MenuItems parseItems(@NotNull FileContext<?> ctx,
                                               @NotNull FileElement e, @Nullable FileObject menuContext,
                                               @NotNull String id){
        if(!(e instanceof FileObject o)) return null;
        return menuModule.getFileMenuItems().deserializeFromFile(ctx, o.get("items"), menuContext,
            itemsFunction(id));
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public @Nullable MenuModule deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e, @Nullable FileObject menuCtx) {
        if(!(e instanceof FileObject o)) return null;
        String id = o.getObject(String.class, "id");
        if(id==null) return null;
        NumberProvider indexes = ctx.getRegistry().deserializeFromFile(NumberProvider.class, o.get("indexes"));
        if(indexes == null) return null;
        MenuItems items = parseItems(ctx, o, menuCtx, id);
        if(items==null) return null;
        return new FillMenuModule(id, this, indexes, items);
    }
}
