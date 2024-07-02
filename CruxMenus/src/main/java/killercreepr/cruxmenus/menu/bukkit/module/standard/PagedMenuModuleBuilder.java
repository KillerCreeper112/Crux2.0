package killercreepr.cruxmenus.menu.bukkit.module.standard;

import killercreepr.crux.Crux;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxmenus.menu.bukkit.module.MenuModule;
import killercreepr.cruxmenus.menu.bukkit.module.MenuModuleBuilder;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PagedMenuModuleBuilder implements MenuModuleBuilder {
    @Override
    public @Nullable MenuModule build(@NotNull FileContext<?> context, @NotNull FileElement e, @Nullable FileObject menuContext) {
        return null;
    }

    public @Nullable NumberProvider parsePageIndexes(@NotNull FileContext<?> context, @NotNull FileElement e, @Nullable FileObject menuContext){
        if(!(e instanceof FileObject o)) return null;
        return o.getObject(NumberProvider.class, "indexes");
    }

    @Override
    public @NotNull Key key() {
        return Crux.key("paged");
    }
}
