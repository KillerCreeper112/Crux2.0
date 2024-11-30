package killercreepr.cruxform.core.shape.cache;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.math.CruxLocation;
import org.bukkit.util.Vector;

import java.util.List;

public class SimpleCachedAddCenter extends SimpleCachedShape{
    protected final Holder<CruxLocation> center;
    public SimpleCachedAddCenter(List<Vector> list, Holder<CruxLocation> center) {
        super(list);
        this.center = center;
    }

    @Override
    public CruxLocation perform(int index) {
        Vector vec = get(index);
        return center.value().add(vec.getX(), vec.getY(), vec.getZ());
    }
}
