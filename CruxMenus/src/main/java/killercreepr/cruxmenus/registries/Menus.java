package killercreepr.cruxmenus.registries;

import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.Registry;
import killercreepr.crux.registry.SimpleMappedRegistry;
import killercreepr.cruxmenus.menu.bukkit.Menu;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Menus {
    public static final MappedRegistry<UUID, Menu> OPENED = new SimpleMappedRegistry<>();
    public static @Nullable Menu getOpened(@NotNull UUID user){
        return OPENED.get(user);
    }

    public static @Nullable Menu getOpened(@NotNull Entity user){
        return getOpened(user.getUniqueId());
    }
}
