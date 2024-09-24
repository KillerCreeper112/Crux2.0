package killercreepr.cruxmenus.core.menu.config.handlers;

import killercreepr.crux.Crux;
import killercreepr.crux.registry.KeyedRegistry;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxmenus.api.menu.config.handler.FileMenuHolder;
import killercreepr.cruxmenus.api.menu.module.MenuModule;
import killercreepr.cruxmenus.api.menu.module.config.MenuModuleBuilder;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileMenuMenuModule extends SimpleFileMenuModuled<MenuModule> {
    protected final @NotNull KeyedRegistry<MenuModuleBuilder> menuModuleBuilders;
    public FileMenuMenuModule(@NotNull FileMenuHolder<?> menuModule, @NotNull KeyedRegistry<MenuModuleBuilder> menuModuleBuilders) {
        super(menuModule);
        this.menuModuleBuilders = menuModuleBuilders;
    }

    @Override
    public @Nullable MenuModule deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e, @Nullable FileObject menuContext) {
        if(!(e instanceof FileObject o)) return null;
        String key = o.getObject(String.class, "type");
        if(key==null) return null;
        Key type = Crux.key(key);

        MenuModuleBuilder builder = menuModuleBuilders.get(type);
        if(builder==null) return null;
        return builder.build(context, e, menuContext);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "menu_module";
    }
}
