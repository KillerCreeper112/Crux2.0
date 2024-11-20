package killercreepr.crux.core.data.world;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class StoredWorld {
    protected final @NotNull UUID worldUUID;
    public StoredWorld(@NotNull UUID worldUUID) {
        this.worldUUID = worldUUID;
    }

    public @Nullable World toBukkitWorld(){
        return Bukkit.getWorld(worldUUID);
    }

    public @NotNull UUID worldUUID() {
        return worldUUID;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        StoredWorld that = (StoredWorld) obj;
        return worldUUID.equals(that.worldUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(worldUUID);
    }
}
