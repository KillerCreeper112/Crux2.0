package killercreepr.cruxblocks.core.hook;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.core.math.BlockPos;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxblocks.core.structure.modules.PlaceCustomBlocksModule;
import killercreepr.cruxblocks.core.structure.modules.UpdateCustomBlocksModule;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.api.structure.module.StructureModule;
import killercreepr.cruxstructures.core.CruxStructuresModule;
import killercreepr.cruxstructures.core.config.FileStructureModule;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public class CruxStructuresHook {
    public static void register(){
        CruxStructuresModule cruxStructures = CruxRegistries.MODULES.getModuleOrThrow(CruxStructuresModule.class);
        FileStructureModule fileStructureModule = cruxStructures.getFileStructureModule();
        fileStructureModule.typeHandlers().register("update_custom_blocks", new PureYamlFileHandler<StructureModule>() {
            @Override
            public @Nullable StructureModule deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
                return new UpdateCustomBlocksModule();
            }
        });

        fileStructureModule.typeHandlers().register("place_custom_blocks", new PureYamlFileHandler<StructureModule>() {
            @Override
            public @Nullable StructureModule deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
                if(!(e instanceof FileObject o)) return null;
                FileRegistry registry = context.getRegistry();
                Map<Key, Collection<BlockPos>> blocks = registry.deserializeFromFile(
                    new TypeToken<Map<Key, Collection<BlockPos>>>(){}.getType(),
                    o.get("blocks")
                );
                return new PlaceCustomBlocksModule(blocks);
            }
        });
    }
}
