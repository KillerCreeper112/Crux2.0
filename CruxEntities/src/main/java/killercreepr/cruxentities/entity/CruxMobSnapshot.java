package killercreepr.cruxentities.entity;

import killercreepr.crux.api.component.DataComponentAccessor;
import killercreepr.crux.api.component.type.EntitySnapshotComponent;
import killercreepr.crux.api.entity.CruxEntity;
import killercreepr.crux.api.entity.CruxEntitySnapshot;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CruxMobSnapshot implements CruxEntitySnapshot {
    protected final @NotNull CruxMob mob;
    protected final DataComponentAccessor components;
    public CruxMobSnapshot(@NotNull CruxMob mob, DataComponentAccessor components) {
        this.mob = mob;
        this.components = components;
    }

    @Override
    public @NotNull Entity createEntity(@NotNull Location to, @Nullable Consumer<Entity> consumer) {
        return mob.spawn(to, e ->{
            if(components != null){
                CruxEntity crux = CruxEntity.entity(e);
                components.forEach(typed ->{
                    if(typed.getValue() instanceof EntitySnapshotComponent c){
                        c.onCreate(crux);
                    }else crux.set(typed);
                });
            }
            if(consumer != null) consumer.accept(e);
        });
    }
}
