package killercreepr.cruxitems.core.config;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.loot.conditions.block.BlockStateCondition;
import killercreepr.cruxconfig.config.bukkit.file.BukkitDataFile;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxconfig.config.bukkit.handler.BukkitCfgHandlers;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.FileLootCondition;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.SimpleFileLootCondition;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.cruxitems.api.item.plugin.PluginItem;
import killercreepr.cruxitems.core.config.handler.FilePluginItem;
import killercreepr.cruxitems.core.config.loader.PluginItemLoader;
import killercreepr.cruxitems.core.loot.condition.ItemStackIsPluginItemCondition;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collection;

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
        registerLoot(BukkitCfgHandlers.LOOT_CONDITION);
    }

    public static void registerHandlers(@NotNull FileRegistry registry){
        registry.registerFileHandler(PluginItem.class, FILE_PLUGIN_ITEM);
    }
    public static void registerLoot(@NotNull FileLootCondition file) {
        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("check_plugin_item")) {
            @Override
            public @NotNull LootCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                //FileRegistry registry = ctx.getRegistry();
                return new ItemStackIsPluginItemCondition(target);
            }
        });
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
