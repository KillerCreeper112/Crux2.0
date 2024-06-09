package killercreepr.cruxentities.modelengine.wrapper;

import killercreepr.cruxentities.wrapper.EntityWrapper;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class DesignEntity extends EntityWrapper implements IDesignEntity {
    public DesignEntity(@NotNull Entity entity) {
        super(entity);
    }
}
