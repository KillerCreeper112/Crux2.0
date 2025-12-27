package killercreepr.crux.core.entity;

import killercreepr.crux.api.component.DataComponentAccessor;
import killercreepr.crux.api.component.type.EntitySnapshotComponent;
import killercreepr.crux.api.entity.CruxEntity;
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
    protected final DataComponentAccessor components;
    public BukkitEntitySnapshot(@NotNull EntityType entityType, DataComponentAccessor components) {
        this.entityType = entityType;
        this.components = components;
    }

    @Override
    public @NotNull Entity createEntity(@NotNull Location to, @Nullable Consumer<Entity> consumer) {
        return to.getWorld().spawnEntity(to, entityType, CreatureSpawnEvent.SpawnReason.CUSTOM, e ->{
            if(components != null){
                CruxEntity crux = CruxEntity.entity(e);
                components.forEach(typed ->{
                    if(typed.getValue() instanceof EntitySnapshotComponent c){
                        c.onCreateEntity(crux);
                    }else crux.set(typed);
                });
            }
            if(consumer != null) consumer.accept(e);
        });
    }
}
