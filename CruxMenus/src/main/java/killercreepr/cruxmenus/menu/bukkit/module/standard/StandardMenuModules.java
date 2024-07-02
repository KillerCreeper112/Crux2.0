package killercreepr.cruxmenus.menu.bukkit.module.standard;

import killercreepr.cruxmenus.menu.bukkit.Menu;
import killercreepr.cruxmenus.menu.bukkit.module.ActiveMenuModule;
import killercreepr.cruxmenus.menu.bukkit.module.MenuModule;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

public class StandardMenuModules {
    public static @NotNull Collection<MenuModule> buildModules(){
        return Set.of(
            new MenuModule() {
                @Override
                public @Nullable ActiveMenuModule build(@NotNull Menu menu) {
                    return null;
                }

                @Override
                public @NotNull Key key() {
                    return null;
                }
            }
        );
    }
}
