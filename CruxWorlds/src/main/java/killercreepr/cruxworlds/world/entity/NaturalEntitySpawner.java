package killercreepr.cruxworlds.world.entity;

import killercreepr.crux.api.math.CruxPosition;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
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
                  @Nullable Consumer<NaturalEntitySpawner> onFinish);

    static @NotNull List<Entity> spawn(@NotNull Collection<? extends NaturalEntitySpawn> poll, @NotNull SpawnContext ctx){
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
                    e = spawnGroup(groupRadius, ctx, s);
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
        for(int x = groupRadius; x >= -groupRadius; --x) {
            for(int y = groupRadius; y >= -groupRadius; --y) {
                for(int z = groupRadius; z >= -groupRadius; --z) {
                    Block b = ctx.getBlock().getRelative(x,y,z);
                    if(b.equals(ctx.getBlock())) continue;
                    SpawnContext groupCtx = SpawnContext.simple(b, ctx.getRandom());
                    if(s.canSpawn(groupCtx)){
                        return s.spawn(groupCtx);
                    }
                }
            }
        }
        return null;
    }
}
