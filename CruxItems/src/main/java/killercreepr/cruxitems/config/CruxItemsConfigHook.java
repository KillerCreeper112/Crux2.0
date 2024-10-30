package killercreepr.cruxitems.config;

import killercreepr.cruxconfig.config.bukkit.file.BukkitDataFile;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.cruxitems.config.handler.FilePluginItem;
import killercreepr.cruxitems.config.loader.PluginItemLoader;
import killercreepr.cruxitems.item.plugin.PluginItem;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class CruxItemsConfigHook {
    private static final FilePluginItem FILE_PLUGIN_ITEM = new FilePluginItem();
    public static FilePluginItem filePluginItem(){
        return FILE_PLUGIN_ITEM;
    }
    public static void register(){
        registerHandlers();
    }

    public static void registerHandlers(){
        CfgRegistries.FILE.forEach(CruxItemsConfigHook::registerHandlers);
    }

    public static void registerHandlers(@NotNull FileRegistry registry){
        registry.registerFileHandler(PluginItem.class, FILE_PLUGIN_ITEM);
    }
    public static void loadCfgPluginItems(@NotNull Plugin plugin, @NotNull String path){
        File f = new CruxFolder(plugin, path).file();
        PluginItemLoader loader = new PluginItemLoader();
        loader.loadConfiguration(f);

        DataFile dataFile = BukkitDataFile.parseFromGeneralPath(f);
        if(dataFile == null) return;
        if(dataFile.getRoot() instanceof FileObject o){
            loader.loadMultipleValues(new FileContext<>(dataFile.fileRegistry()), o);
        }

        /*CruxFolder folder = new CruxFolder(plugin, path);
        File[] files = folder.file().listFiles();
        if(files != null){
            for(File f : files){
                loadCfgPluginItems(plugin, f);
            }
        }
        CruxConfig cfg = new CruxConfig(plugin, path);
        if(!cfg.file().exists()) return;
        if(cfg.getAsYamlObject("") instanceof YamlObject o){
            YamlContext ctx = new YamlContext(cfg.yamlRegistry());
            o.forEach((key, value) ->{
                PluginItem item = FILE_PLUGIN_ITEM.deserialize(
                    ctx, FileElement.fromYaml(value), Crux.key(key)
                );
                if(item == null) return;
                CruxItemRegistries.ITEMS.register(item);
                plugin.getLogger().info("Registered plugin item: " + item.key());
            });
        }*/
    }

    /*public static void loadCfgPluginItems(@NotNull Plugin plugin, @NotNull File f){
        if(f.isDirectory()){
            File[] files = f.listFiles();
            if(files == null) return;
            for(File file : files){
                loadCfgPluginItems(plugin, file);
            }
            return;
        }
        if(!CruxFolder.hasFileExtension(f, "yml")) return;
        PluginItem item = loadCfgPluginItem(new CruxConfig(f));
        if(item == null) return;
        CruxItemRegistries.ITEMS.register(item);
        plugin.getLogger().info("Registered plugin item: " + item.key());
    }

    public static @Nullable PluginItem loadCfgPluginItem(@NotNull CruxConfig cfg){
        return cfg.deserialize(CfgPluginItem.class, "");
    }*/
}
