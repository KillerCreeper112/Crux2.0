package killercreepr.cruxmenus.menu.bukkit.module;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MenuModuleBuilder extends Key {
    @Nullable MenuModule build(@NotNull String id, @NotNull FileContext<?> context, @NotNull FileElement e, @Nullable FileObject menuContext);
}
