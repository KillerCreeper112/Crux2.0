package killercreepr.cruxentities.combat;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class EntityHit {
    public static @NotNull Predicate<Entity> UNDESIRED_BEHAVIOUR(@NotNull Entity p){
        return e -> !e.equals(p);// && GetNearbyEntities.UNDESIRED_BEHAVIOR.test(e) && !TeamUtility.areMatchingOrAlly(p, e) && !NBTTag.IGNORED_TARGET.has(e);
    };

    private final Location start;
    private final Vector direction;
    private final double maxRange;
    private final double thickness;
    private final int maxHitMobs;
    private final boolean ignoreBlocks;

    public EntityHit(@NotNull Location start, double maxRange, double thickness, int maxHitMobs){
        this.start = start;
        this.direction = start.getDirection();
        this.maxRange = maxRange;
        this.thickness = thickness;
        this.maxHitMobs = maxHitMobs;
        this.ignoreBlocks = false;
    }

    public EntityHit(@NotNull Location start, @NotNull Vector direction, double maxRange, double thickness, int maxHitMobs){
        this.start = start;
        this.direction = direction;
        this.maxRange = maxRange;
        this.thickness = thickness;
        this.maxHitMobs = maxHitMobs;
        this.ignoreBlocks = false;
    }

    public EntityHit(@NotNull Location start, double maxRange, double thickness, int maxHitMobs, boolean ignoreBlocks){
        this.start = start;
        this.direction = start.getDirection();
        this.maxRange = maxRange;
        this.thickness = thickness;
        this.maxHitMobs = maxHitMobs;
        this.ignoreBlocks = ignoreBlocks;
    }

    public @Nullable RayTraceResult rayTraceEntity(Location loc, Vector dir, @Nullable Predicate<Entity> predicate, List<Entity> allChecked){
        var entityResult = loc.getWorld().rayTraceEntities(loc, dir, maxRange, thickness, (e -> {
            if(predicate != null && !predicate.test(e)) return false;
            return !allChecked.contains(e);
        }));

        double blockRange = entityResult == null ? maxRange : (loc.toVector().distance(entityResult.getHitPosition()));

        var blockResult = loc.getWorld().rayTraceBlocks(loc, dir, blockRange, FluidCollisionMode.NEVER, true);

        if (entityResult == null) return blockResult;
        if (blockResult == null) return entityResult;

        double entityDistanceSq = entityResult.getHitPosition().distanceSquared(loc.toVector());
        double blockDistanceSq = blockResult.getHitPosition().distanceSquared(loc.toVector());

        if (blockDistanceSq < entityDistanceSq) {
            //Don't check the entity again
            if(entityResult.getHitEntity() != null) allChecked.add(entityResult.getHitEntity());
            return blockResult;
        }

        return entityResult;
    }

    public @NotNull Result getHitEntities(@NotNull final Location loc, @NotNull final Vector dir, @Nullable Predicate<Entity> predicate){
        final List<Entity> allChecked = new ArrayList<>();
        final List<Entity> entities = new ArrayList<>();
        final List<RayTraceResult> results = new ArrayList<>();
        RayTraceResult result;
        for(int maxMobs = maxHitMobs; maxMobs > 0; maxMobs--){
            if(ignoreBlocks){
                result = loc.getWorld().rayTraceEntities(loc, dir, maxMobs, thickness, (e ->{
                    if(predicate != null && !predicate.test(e)) return false;
                    return !allChecked.contains(e);
                }));
            }else{
                result = rayTraceEntity(loc, dir, (e -> {
                        if(predicate != null && !predicate.test(e)) return false;
                        return !allChecked.contains(e);
                    }), allChecked);
            }

            if(result != null){
                results.add(result);
                if(result.getHitEntity() != null){
                    entities.add(result.getHitEntity());
                    allChecked.add(result.getHitEntity());
                }
            }
        }
        return new Result(entities, results);
    }

    public @NotNull Result getHitEntities(@Nullable Predicate<Entity> predicate){
        return getHitEntities(start, direction, predicate);
    }

    public static class Result{
        private final List<Entity> hit;
        private final List<RayTraceResult> results;

        public Result(@NotNull List<Entity> hit, @NotNull List<RayTraceResult> results) {
            this.hit = hit;
            this.results = results;
        }

        public @NotNull List<Entity> getHit() {
            return hit;
        }

        public @NotNull List<RayTraceResult> getResults() {
            return results;
        }

        public boolean isEmpty(){
            return hit.isEmpty() && results.isEmpty();
        }
    }
}
