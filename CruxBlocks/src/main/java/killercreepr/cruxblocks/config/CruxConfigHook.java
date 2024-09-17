package killercreepr.cruxblocks.config;

import killercreepr.crux.Crux;
import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.item.dynamic.components.DynamicPersistTagHandler;
import killercreepr.crux.item.dynamic.components.DynamicPersistentTag;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.component.BushType;
import killercreepr.cruxblocks.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.block.texture.TextureData;
import killercreepr.cruxblocks.config.handler.*;
import killercreepr.cruxblocks.persistence.CruxBlocksPersistTags;
import killercreepr.cruxblocks.persistence.CruxBlocksPersistence;
import killercreepr.cruxblocks.registeries.CruxBlocksRegistries;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxconfig.config.bukkit.handler.impl.FileGenericEnum;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import net.kyori.adventure.key.Key;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Map;

public class CruxConfigHook {
    public static void register(){
        DynamicPersistentTag.HANDLERS.register("block_group", new DynamicPersistTagHandler() {
            @Override
            public <T extends PersistentDataContainer> void handle(@NotNull DynamicPersistentTag tag, @NotNull T to,
                                                                   @Nullable Key key,
                                                                   @NotNull Object value, @NotNull TextParserContext ctx) {
                Key groupKey = Crux.key(ctx.deserializeString(value.toString()));
                CruxBlocksPersistTags.CRUX_BLOCK_GROUP.set(to, CruxBlocksRegistries.BLOCKS.getGroup(groupKey));
            }

            @Override
            public <T extends PersistentDataContainer> DynamicPersistentTag.ParseResult<?> parseObject(@NotNull T object,
                                                                                                                          @NotNull Object value, @NotNull TextParserContext ctx) {
                Key groupKey = Crux.key(ctx.deserializeString(value.toString()));
                return DynamicPersistentTag.ParseResult.result(
                    CruxBlocksPersistence.CRUX_BLOCK_GROUP, CruxBlocksRegistries.BLOCKS.getGroup(groupKey)
                );
            }
        });
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

    public static void loadCfgBlockGroups(@NotNull Plugin plugin, @NotNull String path){
        CruxFolder folder = new CruxFolder(plugin, path);
        File[] files = folder.file().listFiles();
        if(files != null){
            for(File f : files){
                loadCfgBlockGroups(plugin, f);
            }
        }
        CruxConfig cfg = new CruxConfig(plugin, path);
        if(!cfg.file().exists()) return;
        if(cfg.getAsYamlObject("") instanceof YamlObject o){
            YamlContext ctx = new YamlContext(cfg.yamlRegistry());
            o.forEach((key, value) ->{
                CruxBlockGroup item = FileCruxBlockGroup.deserialize(
                    ctx, FileElement.fromYaml(value), Crux.key(key)
                );
                if(item == null) return;
                CruxBlocksRegistries.BLOCKS.registerGroup(item);
                plugin.getLogger().info("Registered block group: " + item.key());
            });
        }
    }

    public static void loadCfgBlockGroups(@NotNull Plugin plugin, @NotNull File f){
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
        CruxBlocksRegistries.BLOCKS.registerGroup(item);
        plugin.getLogger().info("Registered block group: " + item.key());
    }

    public static @Nullable CruxBlockGroup loadCfgBlockGroup(@NotNull CruxConfig cfg){
        return cfg.deserialize(CruxBlockGroup.class, "");
    }
}
