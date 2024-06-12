package killercreepr.crux.util;

import killercreepr.crux.location.LocationHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Predicate;

public abstract class GetNear<T> {
    protected LocationHolder center;
    protected double range;
    protected Integer amount;
    protected Type type;
    protected Predicate<T> filter;

    public GetNear(LocationHolder center) {
        this.center = center;
    }

    public GetNear() {
    }

    public abstract @NotNull Collection<T> get();
    public @Nullable T getFirst(){
        for(T t : get()){
            return t;
        }
        return null;
    }

    public LocationHolder center() {
        return center;
    }

    public void center(LocationHolder center) {
        this.center = center;
    }

    public double range() {
        return range;
    }

    public void range(double range) {
        this.range = range;
    }

    public Predicate<T> filter() {
        return filter;
    }

    public void filter(Predicate<T> filter) {
        this.filter = filter;
    }

    public int amount() {
        return amount;
    }

    public void amount(Integer getAmount) {
        this.amount = getAmount;
    }

    public Type type() {
        return type;
    }

    public void type(Type type) {
        this.type = type;
    }

    public enum Type{
        FARTHEST,
        NEAREST,
        DEFAULT
    }
}
