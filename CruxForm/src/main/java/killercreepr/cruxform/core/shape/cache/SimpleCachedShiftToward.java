package killercreepr.cruxform.core.shape.cache;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.api.math.CruxPosition;
import org.bukkit.util.Vector;

import java.util.List;

public class SimpleCachedShiftToward extends SimpleCachedShape{
    protected final Holder<CruxPosition> start;
    protected final Holder<CruxPosition> end;

    public SimpleCachedShiftToward(List<Vector> list, Holder<CruxPosition> start, Holder<CruxPosition> end) {
        super(list);
        this.start = start;
        this.end = end;
    }


    @Override
    public CruxLocation perform(int index) {
        Vector vec = get(index);
        return CruxLocation.location(start.value()).shiftToward(end.value(), vec.getX());
        //return start.value().shiftToward(end.value(), vec.getX());//todo eventually make up and sideways.
    }
}
