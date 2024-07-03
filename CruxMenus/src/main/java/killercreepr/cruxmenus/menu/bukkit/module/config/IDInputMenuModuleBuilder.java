package killercreepr.cruxmenus.menu.bukkit.module.config;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxmenus.menu.bukkit.module.MenuModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IDInputMenuModuleBuilder extends MenuModuleBuilder {
    @Override
    default @Nullable MenuModule build(@NotNull FileContext<?> context, @NotNull FileElement e, @Nullable FileObject menuContext) {
        if(!(e instanceof FileObject o)) return null;
        String id = o.getObject(String.class, "id");
        if(id==null) return null;
        return build(id, context, o, menuContext);
    }

    @Nullable MenuModule build(@NotNull String id, @NotNull FileContext<?> context, @NotNull FileObject o, @Nullable FileObject menuContext);
}
