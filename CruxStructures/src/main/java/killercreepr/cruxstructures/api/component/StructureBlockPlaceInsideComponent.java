package killercreepr.cruxstructures.api.component;

import killercreepr.cruxstructures.api.structure.StoredStructure;
import org.bukkit.event.block.BlockPlaceEvent;

public interface StructureBlockPlaceInsideComponent extends StructureComponent {
    void onBlockPlace(StoredStructure stored, BlockPlaceEvent event);
}
