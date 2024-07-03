package killercreepr.cruxmenus.menu.bukkit.module;

import killercreepr.crux.registry.SimpleKeyedRegistry;
import killercreepr.crux.registry.SimpleStringIdentifiableRegistry;
import killercreepr.crux.registry.StringIdentifiableRegistry;
import killercreepr.cruxmenus.menu.bukkit.CfgMenu;
import killercreepr.cruxmenus.menu.bukkit.Menu;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

public class MenuModuleRegistryImpl extends SimpleKeyedRegistry<ActiveMenuModule> implements MenuModuleRegistry {
    protected final @NotNull StringIdentifiableRegistry<ActiveMenuModule> byID = new SimpleStringIdentifiableRegistry<>();
    protected final @NotNull Menu menu;
    public MenuModuleRegistryImpl(@NotNull Menu menu) {
        this.menu = menu;
    }

    @Override
    public void load() {
        if(menu instanceof CfgMenu cfg){
            cfg.getHolder().getModules().forEach(m ->{
                ActiveMenuModule module = m.build(menu);
                if(module ==null) return;
                register(module);
            });
        }
        new HashSet<>(this.values()).forEach(m -> m.load(menu));
    }

    @Override
    public void refresh() {
        new HashSet<>(this.values()).forEach(m -> m.refresh(menu));
    }

    @Override
    public @NotNull ActiveMenuModule register(@NotNull ActiveMenuModule object) {
        return super.register(object);
    }

    @Override
    public void onUpdate() {
        new HashSet<>(this.values()).forEach(m -> m.onUpdate(menu));
    }

    @Override
    public void onClose(@NotNull Player p) {
        new HashSet<>(this.values()).forEach(m -> m.onClose(p, menu));
    }

    @Override
    public void onOpen(@NotNull Player p) {
        new HashSet<>(this.values()).forEach(m -> m.onOpen(p, menu));
    }

    @Override
    public @NotNull Menu getMenu() {
        return menu;
    }

    @Override
    public @Nullable ActiveMenuModule getByID(@NotNull String id) {
        return byID.get(id);
    }

    @Override
    public @Nullable ActiveMenuModule unregister(@NotNull Key key) {
        return remove(key);
    }

    @Override
    public @Nullable ActiveMenuModule unregisterByID(@NotNull String id) {
        ActiveMenuModule removed = byID.remove(id);
        if(removed == null) return null;
        return unregister(removed.key());
    }

    @Override
    public boolean unregister(@NotNull ActiveMenuModule object) {
        boolean x = super.unregister(object);
        if(x) byID.unregister(object);
        return x;
    }

    @Override
    public @NotNull ActiveMenuModule register(@NotNull Key key, @NotNull ActiveMenuModule value) {
        byID.register(value);
        return super.register(key, value);
    }

    @Override
    public @Nullable ActiveMenuModule remove(@NotNull Key key) {
        ActiveMenuModule module = super.remove(key);
        if(module != null) byID.unregister(module);
        return module;
    }

    @Override
    public boolean remove(@NotNull Key key, @NotNull ActiveMenuModule value) {
        boolean x = super.remove(key, value);
        if(!x) byID.unregister(value);
        return x;
    }
}
