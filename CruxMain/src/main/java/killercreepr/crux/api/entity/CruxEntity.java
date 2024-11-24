package killercreepr.crux.api.entity;

import killercreepr.crux.api.component.DataComponentHandler;
import killercreepr.crux.core.entity.SimpleCruxEntity;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface CruxEntity extends DataComponentHandler {
    static CruxEntity entity(@NotNull Entity entity){
        return new SimpleCruxEntity(entity);
    }
    @NotNull Entity entity();
}
