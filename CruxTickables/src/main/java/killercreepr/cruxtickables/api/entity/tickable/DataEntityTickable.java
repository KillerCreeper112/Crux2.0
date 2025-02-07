package killercreepr.cruxtickables.api.entity.tickable;

import killercreepr.crux.api.component.parser.hybrid.PersistParser;
import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;

public interface DataEntityTickable extends EntityTickable{
    PersistParser<?> getDataParser();
}
