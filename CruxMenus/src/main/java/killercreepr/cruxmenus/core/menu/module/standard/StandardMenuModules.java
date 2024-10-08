package killercreepr.cruxmenus.core.menu.module.standard;

import killercreepr.crux.Crux;
import killercreepr.cruxmenus.api.menu.module.config.MenuModuleBuilder;
import killercreepr.cruxmenus.api.menu.registry.MenuRegistry;
import killercreepr.cruxmenus.core.menu.module.standard.fill.FillMenuModuleBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

public class StandardMenuModules {
    public static @NotNull Collection<MenuModuleBuilder> buildModules(@NotNull MenuRegistry registry){
        return Set.of(
            new FillMenuModuleBuilder(registry.menuModule(), Crux.key("fill"))
        );
    }
}
