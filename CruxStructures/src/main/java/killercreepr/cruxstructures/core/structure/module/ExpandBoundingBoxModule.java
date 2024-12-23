package killercreepr.cruxstructures.core.structure.module;

import killercreepr.cruxstructures.api.structure.Structure;
import killercreepr.cruxstructures.api.structure.module.StructureBoundingBoxModule;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExpandBoundingBoxModule implements StructureBoundingBoxModule {
    protected final @NotNull Vector expand;
    public ExpandBoundingBoxModule(@NotNull Vector expand) {
        this.expand = expand;
    }

    @Override
    public @Nullable BoundingBox editBoundingBox(@NotNull Structure structure, @NotNull Location at, double rotation, @NotNull BoundingBox box) {
        return box.clone().expand(expand);
    }
}
