package killercreepr.crux.util;

import killercreepr.crux.location.LocationHolder;
import killercreepr.crux.valueproviders.number.ConstantNumber;
import killercreepr.crux.valueproviders.number.NumberHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Predicate;

public abstract class GetNear<T> {
    protected LocationHolder center;
    protected NumberHolder range;
    protected NumberHolder amount;
    protected Operation operation;
    protected Predicate<T> filter;

    public GetNear(LocationHolder center) {
        this.center = center;
    }

    public GetNear() {
    }

    public abstract @NotNull Collection<T> find();
    public @Nullable T findFirst(){
        for(T t : find()){
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

    public NumberHolder range() {
        return range;
    }

    public GetNear<T> range(double range) {
        return range(new ConstantNumber(range));
    }

    public GetNear<T> range(NumberHolder range) {
        this.range = range; return this;
    }

    public Predicate<T> filter() {
        return filter;
    }

    public GetNear<T> filter(Predicate<T> filter) {
        this.filter = filter; return this;
    }

    public NumberHolder amount() {
        return amount;
    }

    public GetNear<T> amount(Integer getAmount) {
        return amount(getAmount==null?null:new ConstantNumber(getAmount));
    }

    public GetNear<T> amount(NumberHolder getAmount) {
        this.amount = getAmount; return this;
    }

    public Operation operation() {
        return operation;
    }

    public GetNear<T> operation(Operation operation) {
        this.operation = operation; return this;
    }

    public enum Operation {
        FARTHEST,
        NEAREST,
        DEFAULT
    }
}
