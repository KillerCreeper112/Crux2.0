package killercreepr.cruxworlds.core.world.entity;

import killercreepr.crux.core.util.CruxEntityUtil;
import killercreepr.cruxworlds.api.world.entity.NaturalEntityGroupPart;
import killercreepr.cruxworlds.api.world.entity.NaturalEntitySpawn;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Predicate;

public abstract class NaturalSpawnPartGroup extends SimpleNaturalEntitySpawnGroup {
    public NaturalSpawnPartGroup(int weight, float quality, @NotNull Collection<NaturalEntitySpawn> spawns) {
        super(weight, quality, spawns);
    }

    public NaturalSpawnPartGroup(int weight, float quality, @NotNull NaturalEntitySpawn... spawns) {
        super(weight, quality, spawns);
    }

    /**
     * @return NaturalEntitySpawns must implement NaturalEntityGroupPart for this to function as intended.
     */
    protected int getEntityAmountNearChunk(@NotNull Chunk chunk, int radius){
        return getEntityAmountNearChunk(chunk, radius, null);
    }
    /**
     * @return NaturalEntitySpawns must implement NaturalEntityGroupPart for this to function as intended.
     */
    protected int getEntityAmountNearChunk(@NotNull Chunk chunk, int radius, @Nullable Predicate<Entity> predicate){
        return CruxEntityUtil.getEntityAmountNearChunk(chunk, radius, e ->{
            if(predicate != null && !predicate.test(e)) return false;
            for(NaturalEntitySpawn s : spawns){
                if(!(s instanceof NaturalEntityGroupPart mob)) continue;
                if(mob.isPartOfGroup(e)) return true;
            }
            return false;
        });
    }
}
