package killercreepr.cruxmenus.core.registries;

import killercreepr.crux.registry.MappedRegistry;
import killercreepr.crux.registry.SimpleMappedRegistry;
import killercreepr.cruxmenus.api.menu.Menu;
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
