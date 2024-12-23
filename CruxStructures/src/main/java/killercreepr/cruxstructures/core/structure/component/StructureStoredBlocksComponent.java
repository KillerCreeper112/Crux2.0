package killercreepr.cruxstructures.core.structure.component;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxstructures.api.component.StructureComponent;
import killercreepr.cruxstructures.api.structure.StoredStructure;
import killercreepr.cruxstructures.core.structure.CfgFAWEStructure;
import org.jetbrains.annotations.NotNull;

public class StructureStoredBlocksComponent implements StructureComponent {
    @Override
    public void onFileLoad(@NotNull FileContext<?> context, @NotNull FileObject o, @NotNull StoredStructure structure) {
        structure.set(StoredStructureComponents.STORED_BLOCKS, new SimpleStoredBlocks((CfgFAWEStructure) structure.getParent()));
    }
}
