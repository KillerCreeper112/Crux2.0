package killercreepr.cruxblocks.api.structure.component;

import killercreepr.cruxblocks.api.event.CruxBlockPlaceEvent;
import killercreepr.cruxstructures.api.component.StructureComponent;
import killercreepr.cruxstructures.api.structure.StoredStructure;

public interface StructureCruxBlockPlaceInsideComponent extends StructureComponent {
    void onCruxBlockPlace(StoredStructure stored, CruxBlockPlaceEvent event);
}
