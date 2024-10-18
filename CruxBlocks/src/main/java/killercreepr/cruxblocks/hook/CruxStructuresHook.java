package killercreepr.cruxblocks.hook;

import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxblocks.structure.modules.UpdateCustomBlocksModule;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.CruxStructuresModule;
import killercreepr.cruxstructures.config.FileStructureModule;
import killercreepr.cruxstructures.structure.module.StructureModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    }
}
