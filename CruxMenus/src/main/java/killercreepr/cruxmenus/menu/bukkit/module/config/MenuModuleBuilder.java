package killercreepr.cruxmenus.menu.bukkit.module.config;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxmenus.menu.bukkit.module.MenuModule;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MenuModuleBuilder extends Keyed {
    @Nullable
    MenuModule build(@NotNull FileContext<?> context, @NotNull FileElement e, @Nullable FileObject menuContext);
}
