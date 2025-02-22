package killercreepr.crux.api.data.world;

import killercreepr.crux.core.data.world.SimpleStoredWorld;
import net.kyori.adventure.key.Key;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface StoredWorld {
    static StoredWorld storedWorld(@NotNull Key worldKey){
        return new SimpleStoredWorld(worldKey);
    }

    static StoredWorld storedWorld(@NotNull World world){
        return storedWorld(world.key());
    }

    @NotNull Key worldKey();
    @Nullable World toBukkitWorld();
}
