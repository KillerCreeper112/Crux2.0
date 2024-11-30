package killercreepr.cruxform.core.shape.cache;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.math.CruxLocation;
import org.bukkit.util.Vector;

import java.util.List;

public class SimpleCachedCircle extends SimpleCachedShape{
    protected final Holder<CruxLocation> center;
    public SimpleCachedCircle(List<Vector> list, Holder<CruxLocation> center) {
        super(list);
        this.center = center;
    }

    @Override
    public CruxLocation perform(int index) {
        CruxLocation location = center.value();
        Vector vec = get(index);
        vec.rotateAroundX(location.pitch());
        vec.rotateAroundY(location.yaw());
        return location.add(vec.getX(), vec.getY(), vec.getZ());
    }
}
