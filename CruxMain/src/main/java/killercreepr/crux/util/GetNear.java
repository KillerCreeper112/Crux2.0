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

    public GetNear<T>  center(LocationHolder center) {
        this.center = center; return this;
    }

    public double range() {
        return range;
    }

    public GetNear<T> range(double range) {
        this.range = range; return this;
    }

    public Predicate<T> filter() {
        return filter;
    }

    public GetNear<T> filter(Predicate<T> filter) {
        this.filter = filter; return this;
    }

    public int amount() {
        return amount;
    }

    public GetNear<T> amount(Integer getAmount) {
        this.amount = getAmount; return this;
    }

    public Type type() {
        return type;
    }

    public GetNear<T> type(Type type) {
        this.type = type; return this;
    }

    public enum Type{
        FARTHEST,
        NEAREST,
        DEFAULT
    }
}
