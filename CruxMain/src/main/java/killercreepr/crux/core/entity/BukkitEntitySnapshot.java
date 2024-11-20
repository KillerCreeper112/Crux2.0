package killercreepr.crux.core.entity;

import killercreepr.crux.api.entity.CruxEntitySnapshot;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BukkitEntitySnapshot implements CruxEntitySnapshot {
    protected final @NotNull EntityType entityType;
    public BukkitEntitySnapshot(@NotNull EntityType entityType) {
        this.entityType = entityType;
    }

    @Override
    public @NotNull Entity createEntity(@NotNull Location to, @Nullable Consumer<Entity> consumer) {
        return to.getWorld().spawnEntity(to, entityType, CreatureSpawnEvent.SpawnReason.CUSTOM, consumer);
    }
}
