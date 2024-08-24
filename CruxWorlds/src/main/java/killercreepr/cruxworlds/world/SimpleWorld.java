package killercreepr.cruxworlds.world;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.UUID;

public class SimpleWorld implements CruxWorld {
    protected final @NotNull World world;
    protected final @NotNull Random random;

    public SimpleWorld(@NotNull World world) {
        this(world, new Random(world.getSeed()));
    }

    public SimpleWorld(@NotNull World world, @NotNull Random random) {
        this.world = world;
        this.random = random;
    }


    @Override
    public @NotNull World toBukkitWorld() {
        return world;
    }

    @Override
    public @NotNull Random getRandom() {

        return random;
    }

    @Override
    public @NotNull String getName() {
        return world.getName();
    }

    @Override
    public @NotNull UUID getUUID() {
        return world.getUID();
    }
}
