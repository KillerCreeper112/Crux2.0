package killercreepr.cruxmenus.menu.bukkit.module;

import killercreepr.crux.registry.Registry;
import killercreepr.cruxmenus.menu.bukkit.CommonMenu;
import killercreepr.cruxmenus.menu.bukkit.Menu;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MenuModuleRegistry extends CommonMenu, Registry<MenuModule> {
    @NotNull
    Menu getMenu();
    @Nullable MenuModule getByID(@NotNull String id);
    @Nullable MenuModule get(@NotNull Key key);
    <T extends MenuModule> T register(@NotNull T module);
    @Nullable MenuModule unregister(@NotNull Key key);
    @Nullable MenuModule unregisterByID(@NotNull String id);
}
