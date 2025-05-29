package killercreepr.cruxworlds.api.world.entity;

import killercreepr.crux.api.math.CruxPosition;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface NaturalEntitySpawner {
    default void navigate(@NotNull World world, @NotNull CruxPosition center){
        navigate(world, center, null, null);
    }
    void navigate(@NotNull World world, @NotNull CruxPosition center,
                  @Nullable Predicate<NaturalEntitySpawner> canContinue,
                  @Nullable Consumer<NaturalEntitySpawner> onFinish,
                  @Nullable Consumer<Entity> spawnConsumer);

    default void navigate(@NotNull World world, @NotNull CruxPosition center,
                  @Nullable Predicate<NaturalEntitySpawner> canContinue,
                  @Nullable Consumer<NaturalEntitySpawner> onFinish){
        navigate(world, center, canContinue, onFinish, null);
    }

    static @NotNull List<Entity> spawn(@NotNull Collection<? extends NaturalEntitySpawn> poll, @NotNull SpawnContext ctx){
        return spawn(poll, ctx, null);
    }

    static @NotNull List<Entity> spawn(@NotNull Collection<? extends NaturalEntitySpawn> poll, @NotNull SpawnContext ctx, @Nullable Consumer<Entity> spawnConsumer){
        List<Entity> list = new ArrayList<>();
        for(NaturalEntitySpawn s : poll){
            int spawned = 0;
            int maxGroup = s.getGroupSize(ctx);
            int groupRadius = s.getGroupRadius(ctx);
            for(int i = 0; i < maxGroup; i++){
                Entity e;
                if(spawned < 1){
                    if(s.canSpawn(ctx)) e = s.spawn(ctx);
                    else break;
                }else{
                    e = spawnGroup(groupRadius, ctx, s, spawnConsumer);
                }
                if(e == null && spawned == 0) break;
                if(e != null){
                    spawned++;
                    list.add(e);
                }
            }
        }
        return list;
    }

    private static void removeAll(Entity e){
        e.remove();
        e.getPassengers().forEach(pass ->{
            if(!pass.isValid()) return;
            removeAll(e);
        });
    }

    static @NotNull List<Entity> spawnCreature(@NotNull Collection<? extends NaturalEntitySpawn> poll, @NotNull SpawnContext ctx, @Nullable Consumer<Entity> spawnConsumer){
        List<Entity> list = new ArrayList<>();
        for(NaturalEntitySpawn s : poll){
            int spawned = 0;
            int maxGroup = s.getGroupSize(ctx);
            int groupRadius = s.getGroupRadius(ctx);
            for(int i = 0; i < maxGroup; i++){
                Entity e;
                if(spawned < 1){
                    if(s.canSpawn(ctx)){
                        e = s.spawn(ctx);
                        if(e instanceof LivingEntity ee){
                            var event = new CreatureSpawnEvent(ee, CreatureSpawnEvent.SpawnReason.NATURAL);
                            if(!event.callEvent()){
                                var veh = e.getVehicle();
                                if(veh != null) removeAll(veh);
                                removeAll(e);
                                e = null;
                            }
                        }
                    }
                    else break;
                }else{
                    e = spawnGroup(groupRadius, ctx, s, spawnConsumer);
                    if(e instanceof LivingEntity ee){
                        var event = new CreatureSpawnEvent(ee, CreatureSpawnEvent.SpawnReason.NATURAL);
                        if(!event.callEvent()){
                            var veh = e.getVehicle();
                            if(veh != null) removeAll(veh);
                            removeAll(e);
                            e = null;
                        }
                    }
                }
                if(e == null && spawned == 0) break;
                if(e != null){
                    spawned++;
                    list.add(e);
                }
            }
        }
        return list;
    }

    static @Nullable Entity spawnGroup(int groupRadius, @NotNull SpawnContext ctx, @NotNull NaturalEntitySpawn s){
        return spawnGroup(groupRadius, ctx, s, null);
    }

    static @Nullable Entity spawnGroup(int groupRadius, @NotNull SpawnContext ctx, @NotNull NaturalEntitySpawn s, @Nullable Consumer<Entity> spawnConsumer){
        for(int x = groupRadius; x >= -groupRadius; --x) {
            for(int y = groupRadius; y >= -groupRadius; --y) {
                for(int z = groupRadius; z >= -groupRadius; --z) {
                    if(x== 0 && y == 0 && z == 0) continue;
                    Block b = ctx.getBlock().getRelative(x,y,z);
                    if(b.equals(ctx.getBlock())) continue;
                    SpawnContext groupCtx = SpawnContext.simple(b, ctx.getRandom());
                    if(s.canSpawn(groupCtx)){
                        Entity e = s.spawn(groupCtx);
                        if(e != null && spawnConsumer != null) spawnConsumer.accept(e);
                        return e;
                    }
                }
            }
        }
        return null;
    }
}
