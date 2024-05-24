package killercreepr.crux.menu.bukkit.registry;

import killercreepr.crux.menu.bukkit.actions.MenuAction;
import killercreepr.crux.menu.bukkit.holder.MenuHolder;
import killercreepr.crux.registry.KeyedRegistry;
import killercreepr.crux.registry.Registry;
import killercreepr.crux.registry.SimpleKeyedRegistry;
import killercreepr.crux.registry.SimpleRegistry;
import killercreepr.crux.tags.format.Format;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;

public class MenuRegistry {
    public final KeyedRegistry<MenuHolder> MENU_HOLDERS = new SimpleKeyedRegistry<>(new HashMap<>()){
        @Override
        public @NotNull MenuHolder register(@NotNull NamespacedKey key, @NotNull MenuHolder value) {
            value.setRegistry(MenuRegistry.this);
            return super.register(key, value);
        }
    };
    public final Registry<MenuAction> MENU_ACTIONS = new SimpleRegistry<>(new HashSet<>());

    private final @NotNull Format format;
    public MenuRegistry(@NotNull Format format) {
        this.format = format;
    }

    public @NotNull Format getFormat() {
        return format;
    }
}
