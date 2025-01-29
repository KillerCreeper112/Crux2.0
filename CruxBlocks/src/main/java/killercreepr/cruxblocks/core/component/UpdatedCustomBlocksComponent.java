package killercreepr.cruxblocks.core.component;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.cruxstructures.api.component.StoredStructureComponent;

import java.util.Collection;

public class UpdatedCustomBlocksComponent implements StoredStructureComponent {
    protected final Collection<CruxPosition> updatedBlocks;

    public UpdatedCustomBlocksComponent(Collection<CruxPosition> updatedBlocks) {
        this.updatedBlocks = updatedBlocks;
    }

    public Collection<CruxPosition> getUpdatedBlocks() {
        return updatedBlocks;
    }
}
