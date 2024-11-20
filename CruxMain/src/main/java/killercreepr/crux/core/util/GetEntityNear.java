package killercreepr.crux.core.util;

import killercreepr.crux.api.data.holder.LocationHolder;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GetEntityNear<T extends Entity> extends GetNear<T> {
    protected final @NotNull Class<T> clazz;
    public GetEntityNear(LocationHolder center, @NotNull Class<T> clazz) {
        super(center);
        this.clazz = clazz;
    }

    public GetEntityNear(@NotNull Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public @NotNull List<T> find() {
        Location center = this.center.value();
        List<T> found = CruxEntityUtil.getEntitiesNear(clazz, center, range.value().doubleValue(), filter);
        switch (operation){
            case NEAREST, FARTHEST ->{
                found = CruxEntityUtil.filterEntityDistance(found, center, amount==null?null:amount.value().intValue(), operation == Operation.FARTHEST);
            }
        }
        return found;
    }
}
