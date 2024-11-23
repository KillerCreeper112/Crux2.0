package killercreepr.cruxworlds.world.entity.impl;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.api.util.CruxWeightedSupplier;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.data.util.Pair;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxworlds.world.entity.NaturalEntitySpawnGroup;
import killercreepr.cruxworlds.world.entity.NaturalEntitySpawner;
import killercreepr.cruxworlds.world.entity.SpawnContext;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SimpleNaturalEntitySpawner implements NaturalEntitySpawner {
    protected final @NotNull Plugin plugin;
    protected final @NotNull Random random;
    protected final @NotNull Collection<NaturalEntitySpawnGroup> registry;
    protected final @NotNull NumberProvider radius;
    protected final @NotNull NumberProvider innerRadius;
    protected final @NotNull NumberProvider spawnCount;
    protected final @NotNull NumberProvider maxSpawnAttempts;
    protected final @NotNull NumberProvider groupSpawnAmount;
    protected final @NotNull NumberProvider yCheck;

    public SimpleNaturalEntitySpawner(@NotNull Plugin plugin, @NotNull Random random, @NotNull Collection<NaturalEntitySpawnGroup> registry,
                                      @NotNull NumberProvider radius, @NotNull NumberProvider innerRadius,
                                      @NotNull NumberProvider spawnCount, @NotNull NumberProvider maxSpawnAttempts,
                                      @NotNull NumberProvider groupSpawnAmount, @NotNull NumberProvider yCheck) {
        this.plugin = plugin;
        this.random = random;
        this.registry = registry;
        this.radius = radius;
        this.innerRadius = innerRadius;
        this.spawnCount = spawnCount;
        this.maxSpawnAttempts = maxSpawnAttempts;
        this.groupSpawnAmount = groupSpawnAmount;
        this.yCheck = yCheck;
    }

    private @Nullable Pair<Collection<NaturalEntitySpawnGroup>, SpawnContext> attemptFind(Block centerBlock, int radius, int innerRadius, int yCheckMin, int yCheckMax, int maxAttempts){
        Pair<Collection<NaturalEntitySpawnGroup>, SpawnContext> attempt = null;
        int attempts = 0;
        while(attempt == null){
            Block b = random(centerBlock, radius, innerRadius);
            SpawnContext ctx = SpawnContext.simple(b, random);
            attempt = attemptFind(b, ctx, yCheckMin, yCheckMax);
            attempts++;
            if(attempts >= maxAttempts) break;
        }
        return attempt;
    }

    private @Nullable Pair<Collection<NaturalEntitySpawnGroup>, SpawnContext> attemptFind(Block b, SpawnContext ctx, int yCheckMin, int yCheckMax){
        Collection<NaturalEntitySpawnGroup> list = CruxWeightedSupplier.builder(registry)
            .rolls(1)
            .filter(check -> check.canSpawn(ctx))
            .build().rollList();
        if(!list.isEmpty()) return Pair.of(list, ctx);
        if(yCheckMin == 0 && yCheckMax == 0) return null;

        Pair<Collection<NaturalEntitySpawnGroup>, SpawnContext> attempt;
        SpawnContext checkCtx;
        for(int y = 1; y <= yCheckMax; y++){
            Block check = b.getRelative(0, y, 0);
            checkCtx = SpawnContext.simple(check, random);
            attempt = attemptFind(check, checkCtx, 0, 0);
            if(attempt != null) return attempt;
        }
        for(int y = 1; y <= yCheckMin; y++){
            Block check = b.getRelative(0, -y, 0);
            checkCtx = SpawnContext.simple(check, random);
            attempt = attemptFind(check, checkCtx, 0, 0);
            if(attempt != null) return attempt;
        }
        return null;
    }

    @Override
    public void navigate(@NotNull World world, @NotNull CruxPosition center,
                         @Nullable Predicate<NaturalEntitySpawner> canContinue,
                         @Nullable Consumer<NaturalEntitySpawner> onFinish){
        if(!center.getBlock(world).getChunk().isLoaded()){
            if(onFinish != null) onFinish.accept(this);
            return;
        }
        int spawnCount = this.spawnCount.value().intValue();
        int radius = this.radius.value().intValue();
        int innerRadius = this.innerRadius.value().intValue();
        int maxAttempts = maxSpawnAttempts.value().intValue();
        Block centerBlock = center.getBlock(world);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, task ->{
            for(int i = 0; i < spawnCount; i++){
                Pair<Collection<NaturalEntitySpawnGroup>, SpawnContext> found = attemptFind(centerBlock, radius, innerRadius, yCheck.getMinValue().intValue(), yCheck.getMaxValue().intValue(), maxAttempts);
                if(found == null) continue;
                plugin.getServer().getScheduler().runTask(plugin, ignored ->{
                    SpawnContext ctx = found.getSecond();
                    for(NaturalEntitySpawnGroup m : found.getFirst()){
                        NaturalEntitySpawner.spawn(
                            m.selectRandom(groupSpawnAmount.value().intValue(), ctx), ctx
                        );
                    }
                });
            }
        });
    }

    public @NotNull Block random(@NotNull Block center, int radius, int innerRadius){
        int x = CruxMath.random(innerRadius, radius, random) * (random.nextBoolean() ? -1 : 1);
        int y = 0;
        int z = CruxMath.random(innerRadius, radius, random) * (random.nextBoolean() ? -1 : 1);
        return center.getRelative(x,y,z);
    }
}
