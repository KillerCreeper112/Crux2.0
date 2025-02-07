package killercreepr.cruxtickables.core.entity.tickable;

import killercreepr.crux.core.data.SimpleKeyed;
import killercreepr.cruxtickables.api.entity.tickable.DataEntityTickable;
import net.kyori.adventure.key.Key;

public abstract class SimpleDataEntityTickable extends SimpleKeyed implements DataEntityTickable {
    public SimpleDataEntityTickable(Key key) {
        super(key);
    }
}
