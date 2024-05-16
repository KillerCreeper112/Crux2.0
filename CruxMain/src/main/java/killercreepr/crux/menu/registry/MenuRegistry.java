package killercreepr.crux.menu.registry;

import killerceepr.crux.menu.actions.MenuAction;
import killerceepr.crux.menu.holder.MenuHolder;
import killerceepr.crux.registry.KeyedRegistry;
import killerceepr.crux.registry.Registry;
import killerceepr.crux.registry.SimpleKeyedRegistry;
import killerceepr.crux.registry.SimpleRegistry;
import killerceepr.crux.tags.format.Format;
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
