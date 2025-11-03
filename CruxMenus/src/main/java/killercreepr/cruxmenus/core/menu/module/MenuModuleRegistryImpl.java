package killercreepr.cruxmenus.core.menu.module;

import killercreepr.cruxmenus.api.menu.CfgMenu;
import killercreepr.cruxmenus.api.menu.Menu;
import killercreepr.cruxmenus.api.menu.module.ActiveMenuModule;
import killercreepr.cruxmenus.api.menu.module.MenuModuleRegistry;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MenuModuleRegistryImpl implements MenuModuleRegistry {
    protected final @NotNull Map<Key, Map<String, ActiveMenuModule>> registry = new LinkedHashMap<>();
    protected final @NotNull Map<String, ActiveMenuModule> byID = new HashMap<>();
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
        this.forEach(m -> m.load(menu));
    }

    @Override
    public void onLoaded() {
        this.forEach(m -> m.onLoaded(menu));
    }

    @Override
    public void refresh() {
        this.forEach(m -> m.refresh(menu));
    }

    @Override
    public void onUpdate() {
        this.forEach(m -> m.onUpdate(menu));
    }

    @Override
    public void onClose(@NotNull HumanEntity p) {
        this.forEach(m -> m.onClose(p, menu));
    }

    @Override
    public void onOpen(@NotNull HumanEntity p) {
        this.forEach(m -> m.onOpen(p, menu));
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
    public @Nullable Collection<ActiveMenuModule> get(@NotNull Key key) {
        var got = registry.get(key);
        return got == null ? null : got.values();
    }

    @Override
    public <T extends ActiveMenuModule> T register(@NotNull T module) {
        registry.computeIfAbsent(module.key(), (d) -> new LinkedHashMap<>()).put(module.id(), module);
        byID.put(module.id(), module);
        return module;
    }

    @Override
    public @Nullable ActiveMenuModule unregister(@NotNull Key key) {
        return null;
    }

    @Override
    public @Nullable ActiveMenuModule unregisterByID(@NotNull String id) {
        ActiveMenuModule removed = byID.remove(id);
        if(removed == null) return null;
        unregister(removed.key());
        return removed;
    }

    @NotNull
    @Override
    public Iterator<ActiveMenuModule> iterator() {
        List<ActiveMenuModule> list = new ArrayList<>();
        registry.values().forEach(d -> list.addAll(d.values()));
        return list.iterator();
    }
}
