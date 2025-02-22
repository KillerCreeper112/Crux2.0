package killercreepr.crux.core.data.world;

import killercreepr.crux.api.data.world.StoredWorld;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SimpleStoredWorld implements StoredWorld {
    protected final @NotNull Key key;
    public SimpleStoredWorld(@NotNull Key key) {
        this.key = key;
    }

    @Override
    public @NotNull Key worldKey() {
        return key;
    }

    @Override
    public @Nullable World toBukkitWorld(){
        return Bukkit.getWorld(key);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SimpleStoredWorld that = (SimpleStoredWorld) obj;
        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
