package killercreepr.crux.api.data;

import killercreepr.crux.api.data.tick.Ticked;

public interface CruxTick extends CruxKeyed, Ticked {
    boolean markedForRemoval();
    CruxTick markRemoval(boolean remove);
}
