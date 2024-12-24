package killercreepr.cruxstructures.core.structure.component;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxstructures.api.component.StoredStructureComponent;
import killercreepr.cruxstructures.api.component.StructureComponent;
import killercreepr.cruxstructures.api.structure.StoredStructure;
import killercreepr.cruxstructures.api.structure.Structure;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class StructureOuterBoxComponent extends SimpleBlockManipulatorComponent implements StructureComponent, StoredStructureComponent {
    protected final @NotNull Vector expand;
    public StructureOuterBoxComponent(boolean disableBlockBreak, boolean disableBlockPlace, @NotNull Vector expand) {
        super(disableBlockBreak, disableBlockPlace);
        this.expand = expand;
    }

    @Override
    public void onFileLoad(@NotNull FileContext<?> context, @NotNull FileObject o, @NotNull StoredStructure structure) {
        if(!(o.get("data") instanceof FileObject data)) return;
        BoundingBox box = context.getRegistry().deserializeFromFile(BoundingBox.class, data.get("outer_box"));
        if(box == null) return;
        structure.set(StoredStructureComponents.OUTER_BOX, box);
    }

    @Override
    public void onFileSave(@NotNull FileContext<?> context, @NotNull FileObject o, @NotNull StoredStructure structure) {
        BoundingBox box = structure.get(StoredStructureComponents.OUTER_BOX);
        if(box == null) return;
        o.add("outer_box", context.getRegistry().serializeToFile(box));
    }

    @Override
    public void onCreated(@NotNull Location center, double rotation, @NotNull StoredStructure stored) {
        BoundingBox outerBox = stored.getBoundingBox().clone().expand(expand);
        stored.set(StoredStructureComponents.OUTER_BOX, outerBox);
    }

    @Override
    public void onStructureHook(@NotNull Structure structure) {
        structure.set(StructureComponents.OUTER_BOX, this);
    }
}
