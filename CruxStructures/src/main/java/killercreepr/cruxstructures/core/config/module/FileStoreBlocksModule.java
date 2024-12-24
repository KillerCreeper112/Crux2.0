package killercreepr.cruxstructures.core.config.module;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.core.structure.component.StructureStoredBlocksComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileStoreBlocksModule extends PureYamlFileHandler<StructureStoredBlocksComponent> {
    @Override
    public @Nullable StructureStoredBlocksComponent deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        return new StructureStoredBlocksComponent();
    }
}
