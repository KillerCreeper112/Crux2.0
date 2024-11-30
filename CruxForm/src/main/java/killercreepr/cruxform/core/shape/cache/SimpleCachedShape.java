package killercreepr.cruxform.core.shape.cache;

import killercreepr.cruxform.api.shape.cache.CreateCachedShape;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

public abstract class SimpleCachedShape implements CreateCachedShape {
    protected final List<Vector> list;
    public SimpleCachedShape(List<Vector> list) {
        this.list = list;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Vector get(int index) {
        return list.get(index);
    }

    @Override
    public boolean has(int index) {
        return index >= 0 && index < list.size();
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @NotNull
    @Override
    public Iterator<Vector> iterator() {
        return list.iterator();
    }
}
