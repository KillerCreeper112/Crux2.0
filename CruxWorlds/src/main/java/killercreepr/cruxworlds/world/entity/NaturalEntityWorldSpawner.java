package killercreepr.cruxworlds.world.entity;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface NaturalEntityWorldSpawner extends NaturalEntitySpawner {
    @NotNull
    CompletableFuture<Boolean> checkCanNavigate(@NotNull World world);

    boolean canNavigate(@NotNull World world);
    int getRadius();
    int getInnerRadius();
    int getGlobalMobLimit();
    boolean isBelowGlobalMobLimit(int amount);
    int getNaturallySpawnedMobs(@NotNull World world);
}
