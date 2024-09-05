package killercreepr.cruxentities.modelengine.wrapper;

import killercreepr.cruxentities.wrapper.EntityWrapper;
import killercreepr.cruxentities.wrapper.IEntityWrapper;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class DesignEntity extends EntityWrapper implements IDesignEntity {
    public DesignEntity(@NotNull Entity entity) {
        super(entity);
    }
}
