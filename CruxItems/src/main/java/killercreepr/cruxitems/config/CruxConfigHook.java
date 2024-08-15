package killercreepr.cruxitems.config;

import killercreepr.crux.Crux;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.cruxitems.config.handler.FilePluginItem;
import killercreepr.cruxitems.item.plugin.CfgPluginItem;
import killercreepr.cruxitems.item.plugin.PluginItem;
import killercreepr.cruxitems.registries.CruxItemRegistries;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class CruxConfigHook {
    public static void register(){
        registerHandlers();
    }

    public static void registerHandlers(){
        CfgRegistries.FILE.forEach(CruxConfigHook::registerHandlers);
    }

    public static void registerHandlers(@NotNull FileRegistry registry){
        registry.registerFileHandler(PluginItem.class, new FilePluginItem());
    }

    public static void loadCfgPluginItems(@NotNull Plugin plugin, @NotNull String path){
        CruxFolder folder = new CruxFolder(plugin, path);
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
                PluginItem item = FilePluginItem.deserialize(
                    ctx, FileElement.fromYaml(value), Crux.key(key)
                );
                if(item == null) return;
                CruxItemRegistries.ITEMS.register(item);
                plugin.getLogger().info("Registered plugin item: " + item.key());
            });
        }
    }

    public static void loadCfgPluginItems(@NotNull Plugin plugin, @NotNull File f){
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
    }
}
