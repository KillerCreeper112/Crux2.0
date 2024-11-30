package killercreepr.cruxform.core.shape.cache;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.math.CruxLocation;
import org.bukkit.util.Vector;

import java.util.List;

public class SimpleCachedAddRelative extends SimpleCachedShape{
    protected final Holder<CruxLocation> center;
    public SimpleCachedAddRelative(List<Vector> list, Holder<CruxLocation> center) {
        super(list);
        this.center = center;
    }

    @Override
    public CruxLocation perform(int index) {
        Vector vec = get(index);
        return center.value().addRelative(vec.getX(), vec.getY(), vec.getZ());
    }
}
