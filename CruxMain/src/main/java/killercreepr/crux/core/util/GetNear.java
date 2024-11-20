package killercreepr.crux.core.util;

import killercreepr.crux.api.data.holder.LocationHolder;
import killercreepr.crux.api.valueproviders.number.NumberHolder;
import killercreepr.crux.core.location.EntityLocation;
import killercreepr.crux.core.location.StaticLocation;
import killercreepr.crux.core.valueproviders.number.ConstantNumber;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public abstract class GetNear<T> {
    protected LocationHolder center;
    protected NumberHolder range;
    protected NumberHolder amount;
    protected Operation operation = Operation.DEFAULT;
    protected Predicate<T> filter;

    public GetNear(LocationHolder center) {
        this.center = center;
    }

    public GetNear() {
    }

    public abstract @NotNull List<T> find();
    public @Nullable T findFirst(){
        for(T t : find()){
            return t;
        }
        return null;
    }

    public LocationHolder center() {
        return center;
    }

    public GetNear<T> center(LocationHolder center) {
        this.center = center; return this;
    }

    public GetNear<T> center(Entity center) {
        return center((LocationHolder)  (center==null?null:new EntityLocation(center)));
    }

    public GetNear<T> center(Location center) {
        return center((LocationHolder) (center==null?null:new StaticLocation(center)));
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
