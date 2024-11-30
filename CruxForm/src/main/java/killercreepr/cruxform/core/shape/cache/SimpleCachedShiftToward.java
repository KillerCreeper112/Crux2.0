package killercreepr.cruxform.core.shape.cache;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.math.CruxLocation;
import org.bukkit.util.Vector;

import java.util.List;

public class SimpleCachedShiftToward extends SimpleCachedShape{
    protected final Holder<CruxLocation> start;
    protected final Holder<CruxLocation> end;

    public SimpleCachedShiftToward(List<Vector> list, Holder<CruxLocation> start, Holder<CruxLocation> end) {
        super(list);
        this.start = start;
        this.end = end;
    }


    @Override
    public CruxLocation perform(int index) {
        Vector vec = get(index);
        return start.value().shiftToward(end.value(), vec.getX());//todo eventually make up and sideways.
    }
}
