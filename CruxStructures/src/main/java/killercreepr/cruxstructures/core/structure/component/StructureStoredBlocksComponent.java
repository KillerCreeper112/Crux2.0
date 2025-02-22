package killercreepr.cruxstructures.core.structure.component;

import killercreepr.crux.api.data.world.StoredChunk;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxstructures.api.component.StructureComponent;
import killercreepr.cruxstructures.api.structure.StoredStructure;
import killercreepr.cruxstructures.core.structure.CfgFAWEStructure;
import org.jetbrains.annotations.NotNull;

public class StructureStoredBlocksComponent extends SimpleBlockManipulatorComponent implements StructureComponent {
    public StructureStoredBlocksComponent(boolean disableBlockBreak, boolean disableBlockPlace) {
        super(disableBlockBreak, disableBlockPlace);
    }

    @Override
    public void onFileLoad(@NotNull FileContext<?> context, @NotNull FileObject o, @NotNull StoredStructure structure) {
        structure.set(StoredStructureComponents.STORE_BLOCKS, new SimpleStoredBlocks(disableBlockBreak, disableBlockPlace, (CfgFAWEStructure) structure.getParent()));
    }

    @Override
    public void onCreated(@NotNull StoredChunk chunk, @NotNull CruxPosition center, double rotation, @NotNull StoredStructure stored) {
        stored.set(StoredStructureComponents.STORE_BLOCKS, new SimpleStoredBlocks(disableBlockBreak, disableBlockPlace, (CfgFAWEStructure) stored.getParent()));
    }
}
