package killercreepr.cruxblocks.core.config;

import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.component.BushType;
import killercreepr.cruxblocks.api.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.api.block.texture.TextureData;
import killercreepr.cruxblocks.core.config.handler.*;
import killercreepr.cruxblocks.core.config.loader.CruxBlockGroupLoader;
import killercreepr.cruxconfig.config.bukkit.file.BukkitDataFile;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxconfig.config.bukkit.handler.impl.FileGenericEnum;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Map;

public class CruxConfigHook {
    public static void register(){
        registerHandlers();
    }

    public static void registerHandlers(){
        CfgRegistries.FILE.forEach(CruxConfigHook::registerHandlers);
    }

    public static final FileNoteTextureData NOTE_TEXTURE_DATA = new FileNoteTextureData();
    public static final FileWireTextureData WIRE_TEXTURE_DATA = new FileWireTextureData();
    public static final FileNoteTextureData MATERIAL_TEXTURE_DATA = new FileNoteTextureData();//todo
    public static void registerHandlers(@NotNull FileRegistry registry){
        registry.registerFileHandler(TextureData.class, new FileTextureData(Map.of(
            "note", NOTE_TEXTURE_DATA,
            "wire", WIRE_TEXTURE_DATA,
            "material", MATERIAL_TEXTURE_DATA
        )));
        registry.registerFileHandler(CruxBlock.class, new FileCruxBlock());
        registry.registerFileHandler(CruxBlockGroup.class, new FileCruxBlockGroup());
        registry.registerFileHandler(BushType.class, new FileGenericEnum<>(BushType.class));
    }

    public static final CruxBlockGroupLoader loader = new CruxBlockGroupLoader();
    public static void loadCfgBlockGroups(@NotNull Plugin plugin, @NotNull String path){
        loader.unregisterRegisteredGroups();
        File f = new CruxFolder(plugin, path).file();
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
                loadCfgBlockGroups(plugin, f);
            }
        }*/
        /*CruxConfig cfg = new CruxConfig(plugin, path);
        if(!cfg.file().exists()) return;
        if(cfg.getAsYamlObject("") instanceof YamlObject o){
            YamlContext ctx = new YamlContext(cfg.yamlRegistry());
            o.forEach((key, value) ->{
                FileElement fileElement = FileElement.fromYaml(value);
                CruxBlockGroup item = FileCruxBlockGroup.deserialize(
                    ctx, fileElement, Crux.key(key)
                );
                if(item != null) item = ctx.getRegistry().getParsedObjectRegistry().parse(fileElement, ctx, item);
                if(item == null) return;
                CruxBlocksRegistries.BLOCK.registerGroup(item);
                plugin.getLogger().info("Registered block group: " + item.key());
            });
        }*/
    }

    /*public static void loadCfgBlockGroups(@NotNull Plugin plugin, @NotNull File f){
        if(f.isDirectory()){
            File[] files = f.listFiles();
            if(files == null) return;
            for(File file : files){
                loadCfgBlockGroups(plugin, file);
            }
            return;
        }
        if(!CruxFolder.hasFileExtension(f, "yml")) return;
        CruxBlockGroup item = loadCfgBlockGroup(new CruxConfig(f));
        if(item == null) return;
        CruxBlocksRegistries.BLOCK.registerGroup(item);
        plugin.getLogger().info("Registered block group: " + item.key());
    }

    public static @Nullable CruxBlockGroup loadCfgBlockGroup(@NotNull CruxConfig cfg){
        return cfg.deserialize(CruxBlockGroup.class, "");
    }*/
}
