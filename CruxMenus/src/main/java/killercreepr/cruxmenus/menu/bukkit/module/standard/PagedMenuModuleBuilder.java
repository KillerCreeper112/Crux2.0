package killercreepr.cruxmenus.menu.bukkit.module.standard;

import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxmenus.menu.bukkit.module.SimpleMenuModuleBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PagedMenuModuleBuilder implements SimpleMenuModuleBuilder {
    public @Nullable NumberProvider parsePageIndexes(@NotNull FileContext<?> context,
                                                     @NotNull FileElement e,
                                                     @Nullable FileObject menuContext){
        if(!(e instanceof FileObject o)) return null;
        return o.getObject(NumberProvider.class, "indexes");
    }
}
