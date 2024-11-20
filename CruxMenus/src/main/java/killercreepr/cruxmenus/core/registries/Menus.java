package killercreepr.cruxmenus.core.registries;

import killercreepr.crux.api.registry.MappedRegistry;
import killercreepr.crux.core.registry.SimpleMappedRegistry;
import killercreepr.cruxmenus.api.menu.Menu;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;

public class Menus {
    public static final MappedRegistry<UUID, Menu> OPENED = new SimpleMappedRegistry<>();
    public static @Nullable Menu getOpened(@NotNull UUID user){
        return OPENED.get(user);
    }

    public static @Nullable Menu getOpened(@NotNull Entity user){
        return getOpened(user.getUniqueId());
    }

    public static @Nullable Menu refresh(@NotNull Entity user){
        return refresh(user.getUniqueId());
    }

    public static @Nullable Menu refresh(@NotNull UUID user){
        Menu menu = getOpened(user);
        if(menu == null) return null;
        menu.refresh();
        return menu;
    }

    public static @Nullable Menu apply(@NotNull Entity user, @NotNull Consumer<Menu> consumer){
        return apply(user.getUniqueId(), consumer);
    }

    public static @Nullable Menu apply(@NotNull UUID user, @NotNull Consumer<Menu> consumer){
        Menu menu = getOpened(user);
        if(menu == null) return null;
        consumer.accept(menu);
        return menu;
    }
}
