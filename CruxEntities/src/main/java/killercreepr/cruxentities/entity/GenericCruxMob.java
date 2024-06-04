package killercreepr.cruxentities.entity;

import killercreepr.cruxentities.persistence.EntityPersistTags;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public abstract class GenericCruxMob implements CruxMob{
    protected final @NotNull Key key;
    public GenericCruxMob(@NotNull Key key) {
        this.key = key;
    }

    @Override
    public final @NotNull Entity spawn(@NotNull Location at) {
        Entity e = spawnAt(at);
        EntityPersistTags.ENTITY.set(e, key);
        return e;
    }

    protected abstract @NotNull Entity spawnAt(@NotNull Location at);

    @Override
    public @NotNull Key key() {
        return key;
    }
}
