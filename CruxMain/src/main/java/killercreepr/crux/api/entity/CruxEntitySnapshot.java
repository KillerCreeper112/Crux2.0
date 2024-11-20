package killercreepr.crux.api.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface CruxEntitySnapshot {
    default @NotNull
    Entity createEntity(@NotNull Location to){
        return createEntity(to, null);
    }
    @NotNull
    Entity createEntity(@NotNull Location to, @Nullable Consumer<Entity> consumer);
}
