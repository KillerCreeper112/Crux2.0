package killercreepr.cruxform.api.shape.cache;

import killercreepr.crux.api.math.CruxPosition;
import org.bukkit.util.Vector;

public interface CreateCachedShape extends Iterable<Vector> {
    Vector get(int index);
    boolean has(int index);
    int size();
    CruxPosition perform(int index);
}
