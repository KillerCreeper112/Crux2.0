package killercreepr.cruxtickables.api.entity.tickable;

import killercreepr.crux.api.component.parser.hybrid.PersistParser;

public interface DataEntityTickable extends EntityTickable{
    PersistParser<?> getDataParser();
}
