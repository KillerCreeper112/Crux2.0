package killercreepr.cruxblocks.core.component;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.cruxstructures.api.component.StoredStructureComponent;

import java.util.Collection;

public class PlacedCustomBlocksComponent implements StoredStructureComponent {
    protected final Collection<CruxPosition> placedBlocks;

    public PlacedCustomBlocksComponent(Collection<CruxPosition> placedBlocks) {
        this.placedBlocks = placedBlocks;
    }

    public Collection<CruxPosition> getPlacedBlocks() {
        return placedBlocks;
    }
}
