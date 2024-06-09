package killercreepr.cruxentities.wrapper;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface IEntityWrapper {
    /**
     * @throws ClassCastException if the object is not null and is not assignable to the type T.
     */
    default <T extends Entity> @NotNull T toClass(@NotNull Class<T> type){
        return type.cast(entity());
    }

    @NotNull Entity entity();
}
