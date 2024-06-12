package killercreepr.crux.util;

import killercreepr.crux.location.LocationHolder;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
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
    public @NotNull Collection<T> get() {
        Location center = this.center.value();
        List<T> found = CruxEntity.getEntitiesNear(clazz, center, range, filter);
        switch (type){
            case NEAREST, FARTHEST ->{
                found = CruxEntity.filterEntityDistance(found, center, amount, type == Type.FARTHEST);
            }
        }
        return found;
    }
}
