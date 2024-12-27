package killercreepr.cruxstructures.core.structure.component;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.data.world.StoredChunk;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxstructures.api.component.StoredStructureComponent;
import killercreepr.cruxstructures.api.component.StructureComponent;
import killercreepr.cruxstructures.api.structure.StoredStructure;
import killercreepr.cruxstructures.api.structure.Structure;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StructureOuterBoxComponent extends SimpleBlockManipulatorComponent implements StructureComponent, StoredStructureComponent {
    protected final @Nullable Vector expand;
    protected final @Nullable Vector offset;
    protected final @Nullable Vector expandNegative;
    protected final @Nullable Vector expandPostive;
    public StructureOuterBoxComponent(boolean disableBlockBreak, boolean disableBlockPlace, @Nullable Vector expand, @Nullable Vector offset, @Nullable Vector expandNegative, @Nullable Vector expandPostive) {
        super(disableBlockBreak, disableBlockPlace);
        this.expand = expand;
        this.offset = offset;
        this.expandNegative = expandNegative;
        this.expandPostive = expandPostive;
    }

    @Override
    public void onFileLoad(@NotNull FileContext<?> context, @NotNull FileObject o, @NotNull StoredStructure structure) {
        BoundingBox outerBox = buildBox(structure);
        structure.set(StoredStructureComponents.OUTER_BOX, outerBox);
    }

    @Override
    public void onCreated(@NotNull StoredChunk chunk, @NotNull CruxPosition center, double rotation, @NotNull StoredStructure stored) {
        BoundingBox outerBox = buildBox(stored);
        stored.set(StoredStructureComponents.OUTER_BOX, outerBox);
    }

    public BoundingBox buildBox(@NotNull StoredStructure stored){
        BoundingBox box = stored.getBoundingBox().clone();
        if(expand != null) box.expand(expand);
        if(offset != null) box.shift(offset);
        return box;
    }

    @Override
    public void onStructureHook(@NotNull Structure structure) {
        structure.set(StructureComponents.OUTER_BOX, this);
    }
}
