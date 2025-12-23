package killercreepr.cruxworlds.core.component;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.cruxworlds.api.component.EntitySpawnComponent;
import killercreepr.cruxworlds.api.world.entity.NaturalEntitySpawn;
import killercreepr.cruxworlds.api.world.entity.SpawnContext;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EntitySpawnPassengers implements EntitySpawnComponent {
    public final List<NaturalEntitySpawn> passengers;
    public final boolean append;

    public EntitySpawnPassengers(List<NaturalEntitySpawn> passengers, boolean append) {
        this.passengers = passengers;
        this.append = append;
    }

    @Override
    public void onCreateEntity(@NotNull SpawnContext ctx, Entity e, TextParserContext textCtx) {
        Entity lastPassenger = null;
        for (NaturalEntitySpawn spawn : passengers) {
            Entity spawned = spawn.spawn(ctx);
            if(spawned == null) return;

            if(append && lastPassenger != null){
                lastPassenger.addPassenger(spawned);
            }else e.addPassenger(spawned);

            lastPassenger = spawned;
        }
    }
}
