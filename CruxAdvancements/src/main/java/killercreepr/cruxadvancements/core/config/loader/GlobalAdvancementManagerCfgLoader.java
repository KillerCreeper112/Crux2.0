package killercreepr.cruxadvancements.core.config.loader;

import killercreepr.crux.core.Crux;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.cruxadvancements.api.advancement.manager.CruxAdvancementManager;
import killercreepr.cruxadvancements.core.config.CfgSimpleAdvancementManager;
import killercreepr.cruxadvancements.core.registries.AdvancementRegistries;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Level;

public class GlobalAdvancementManagerCfgLoader {
    protected final CruxPlugin plugin;
    protected final String fileLoadingPath;
    public GlobalAdvancementManagerCfgLoader(CruxPlugin plugin, String fileLoadingPath) {
        this.plugin = plugin;
        this.fileLoadingPath = fileLoadingPath;
    }

    protected final Map<Key, CruxAdvancementManager<?>> advancementManagers = new HashMap<>();


    public Map<Key, CruxAdvancementManager<?>> getAdvancementManagers() {
        return advancementManagers;
    }

    public void load(Player p){
        advancementManagers.values().forEach(advancementManager ->{
            advancementManager.loadProgress(p.getUniqueId());
        });
    }

    public void saveAndUnload(Collection<? extends Player> p){
        advancementManagers.values().forEach(advancementManager ->{
            advancementManager.save();
        });
        //p.forEach(this::saveAndUnload);
    }

    public void saveAndUnload(Player p){
        saveAndUnload(p.getUniqueId());
    }
    public void save(Player p){
        advancementManagers.values().forEach(advancementManager ->{
            advancementManager.saveProgress(p.getUniqueId());
        });
    }
    public void save(UUID p){
        advancementManagers.values().forEach(advancementManager ->{
            advancementManager.saveProgress(p);
        });
    }
    public void saveAndUnload(UUID p){
        advancementManagers.values().forEach(advancementManager ->{
            advancementManager.saveProgress(p);
            advancementManager.unloadProgress(p);
        });
    }

    public void load(){
        advancementManagers.values().forEach(c ->{
            c.load(plugin);
        });
    }

    public void refresh(){
        refresh(null);
    }

    public void refresh(Consumer<CruxAdvancementManager<?>> loadConsumer){
        loadConfiguration(CruxFolder.file(plugin, fileLoadingPath));
        advancementManagers.values().forEach(c -> c.refresh(plugin, loadConsumer));
    }

    public void loadConfiguration(@NotNull File folder){
        if(!folder.exists()) return;
        File[] files = folder.listFiles();
        if(files == null) return;
        for(File f : files){
            if(!f.isDirectory()) continue;
            Key key = plugin.key(CruxFolder.withoutFileExtension(f.getName()));
            if(advancementManagers.containsKey(key)) continue;

            CfgSimpleAdvancementManager manager = CfgSimpleAdvancementManager.createNew(key, plugin, fileLoadingPath);
            advancementManagers.put(manager.key(), manager);
            AdvancementRegistries.ADVANCEMENT_MANAGERS.register(manager);
            Crux.log(Level.INFO, "Registered CfgSimpleAdvancementManager: " + manager.key());

            //manager.load(plugin);
        }
    }

    public CruxPlugin getPlugin() {
        return plugin;
    }

}

