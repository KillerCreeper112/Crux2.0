package killercreepr.cruxstructures.core.structure.component;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxstructures.api.component.StoredStructureComponent;
import killercreepr.cruxstructures.api.component.StructureComponent;
import killercreepr.cruxstructures.api.structure.StoredStructure;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class StructureDisableBlockBreakComponent implements StructureComponent, StoredStructureComponent {
    @Override
    public void onFileLoad(@NotNull FileContext<?> context, @NotNull FileObject o, @NotNull StoredStructure structure) {
        structure.set(StoredStructureComponents.DISABLE_BLOCK_BREAK, true);
    }

    @Override
    public void onLoad(@NotNull StoredStructure structure) {
        structure.set(StoredStructureComponents.DISABLE_BLOCK_BREAK, true);
    }

    @Override
    public void onCreated(@NotNull Location center, double rotation, @NotNull StoredStructure stored) {
        stored.set(StoredStructureComponents.DISABLE_BLOCK_BREAK, true);
    }
}
