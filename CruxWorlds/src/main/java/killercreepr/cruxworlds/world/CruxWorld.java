package killercreepr.cruxworlds.world;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.UUID;

public interface CruxWorld {
    @NotNull
    World toBukkitWorld();
    @NotNull
    Random getRandom();
    @NotNull String getName();
    @NotNull UUID getUUID();

    default void onInitiate(){}
    default void onLoad(){}
    default void onUnload(){}
    default void onSave(){}
}
