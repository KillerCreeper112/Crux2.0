package killercreepr.cruxadvancements.config.loader;

import killercreepr.crux.Crux;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.cruxadvancements.crazy.CfgCrazyAdvancementManager;
import killercreepr.cruxadvancements.registries.AdvancementRegistries;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class CfgCrazyAdvancementManagerCfgLoader {
    protected final CruxPlugin plugin;
    public CfgCrazyAdvancementManagerCfgLoader(CruxPlugin plugin) {
        this.plugin = plugin;
    }

    protected final Map<Key, CfgCrazyAdvancementManager> advancementManagers = new HashMap<>();

    public void load(Player p){
        advancementManagers.values().forEach(advancementManager ->{
            advancementManager.loadProgress(p.getUniqueId());
            advancementManager.getCrazyManager().addPlayer(p);
        });
    }

    public void saveAndUnload(Collection<? extends Player> p){
        p.forEach(this::saveAndUnload);
    }

    public void saveAndUnload(Player p){
        advancementManagers.values().forEach(advancementManager ->{
            advancementManager.saveProgress(p.getUniqueId());
            advancementManager.getCrazyManager().removePlayer(p);
            advancementManager.unloadProgress(p.getUniqueId());
        });
    }

    public void load(){
        advancementManagers.values().forEach(c ->{
            c.load(plugin);
            c.loadAllCrazyAdvancements();
        });
    }

    public void refresh(){
        loadConfiguration(CruxFolder.file(plugin, "advancements"));
        advancementManagers.values().forEach(c -> c.refresh(plugin));
    }

    public void loadConfiguration(@NotNull File folder){
        if(!folder.exists()) return;
        File[] files = folder.listFiles();
        if(files == null) return;
        for(File f : files){
            if(!f.isDirectory()) continue;
            Key key = plugin.key(CruxFolder.withoutFileExtension(f.getName()));
            if(advancementManagers.containsKey(key)) continue;

            CfgCrazyAdvancementManager manager = CfgCrazyAdvancementManager.createNew(key, plugin);
            advancementManagers.put(manager.key(), manager);
            AdvancementRegistries.ADVANCEMENT_MANAGERS.register(manager);
            Crux.log(Level.INFO, "Registered CfgCrazyAdvancementManager: " + manager.key());

            //manager.load(plugin);
        }
    }

    public CruxPlugin getPlugin() {
        return plugin;
    }

}

