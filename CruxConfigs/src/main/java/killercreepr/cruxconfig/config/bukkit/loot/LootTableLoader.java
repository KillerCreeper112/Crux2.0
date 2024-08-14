package killercreepr.cruxconfig.config.bukkit.loot;

import killercreepr.crux.Crux;
import killercreepr.crux.loot.item.api.ItemLootTable;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxconfig.config.bukkit.handler.BukkitCfgHandlers;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.logging.Level;

public class LootTableLoader {
    public void loadConfiguration(@NotNull File folder){
        loadConfiguration(folder, null);
    }

    public void loadConfiguration(@NotNull File file, @Nullable String path){
        if(CruxFolder.hasFileExtension(file, "yml")){
            loadConfiguration(new CruxConfig(file), path == null ? file.getName() : path + "/" + CruxFolder.withoutFileExtension(file.getName()));
            return;
        }
        File[] list = file.listFiles();
        if(list==null) return;
        for(File f : list){
            if(f.isDirectory()) path = path == null ? f.getName() : (path + "/" + f.getName());
            loadConfiguration(f, path);
        }
    }

    public void loadConfiguration(@NotNull CruxConfig cfg, @Nullable String path){
        ItemLootTable table;
        if(path == null) table = cfg.deserialize(ItemLootTable.class, "");
        else{
            YamlObject root = cfg.getRootAsYamlObject();
            if(root==null) return;

            table = BukkitCfgHandlers.ITEM_LOOT_TABLE.deserializeFromFile(
                new YamlContext(cfg.yamlRegistry()), (FileObject) FileElement.fromYaml(root),
                Crux.key(path)
            );
        }
        if(table == null) return;
        Crux.log(Level.INFO, "Registered loot table: " + table.key());
        CruxRegistries.ITEM_LOOT_TABLE.register(table);
    }
}
