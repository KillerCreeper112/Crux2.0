package killercreepr.cruxmenus.menu.bukkit.module;

import killercreepr.crux.registry.SimpleKeyedRegistry;
import killercreepr.crux.registry.SimpleStringIdentifiableRegistry;
import killercreepr.crux.registry.StringIdentifiableRegistry;
import killercreepr.cruxmenus.menu.bukkit.Menu;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MenuModuleRegistryImpl extends SimpleKeyedRegistry<MenuModule> implements MenuModuleRegistry {
    protected final @NotNull StringIdentifiableRegistry<MenuModule> byID = new SimpleStringIdentifiableRegistry<>();
    protected final @NotNull Menu menu;
    public MenuModuleRegistryImpl(@NotNull Menu menu) {
        this.menu = menu;
    }

    @Override
    public void load() {
        this.forEach(m -> m.load(menu));
    }

    @Override
    public void onUpdate() {
        this.forEach(m -> m.onUpdate(menu));
    }

    @Override
    public void onClose(@NotNull Player p) {
        this.forEach(m -> m.onClose(p, menu));
    }

    @Override
    public void onOpen(@NotNull Player p) {
        this.forEach(m -> m.onOpen(p, menu));
    }

    @Override
    public @NotNull Menu getMenu() {
        return menu;
    }

    @Override
    public @Nullable MenuModule getByID(@NotNull String id) {
        return byID.get(id);
    }

    @Override
    public @Nullable MenuModule unregister(@NotNull Key key) {
        return remove(key);
    }

    @Override
    public @Nullable MenuModule unregisterByID(@NotNull String id) {
        MenuModule removed = byID.remove(id);
        if(removed == null) return null;
        return unregister(removed.key());
    }

    @Override
    public boolean unregister(@NotNull MenuModule object) {
        boolean x = super.unregister(object);
        if(x) byID.unregister(object);
        return x;
    }

    @Override
    public @NotNull MenuModule register(@NotNull Key key, @NotNull MenuModule value) {
        byID.register(value);
        return super.register(key, value);
    }

    @Override
    public @Nullable MenuModule remove(@NotNull Key key) {
        MenuModule module = super.remove(key);
        if(module != null) byID.unregister(module);
        return module;
    }

    @Override
    public boolean remove(@NotNull Key key, @NotNull MenuModule value) {
        boolean x = super.remove(key, value);
        if(!x) byID.unregister(value);
        return x;
    }
}
