package killercreepr.cruxtickables.core.entity.tickable;

import killercreepr.crux.core.data.SimpleKeyed;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickable;
import net.kyori.adventure.key.Key;

public abstract class SimpleEntityTickable extends SimpleKeyed implements EntityTickable {
    public SimpleEntityTickable(Key key) {
        super(key);
    }
}
