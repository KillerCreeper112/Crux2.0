package killercreepr.cruxentities.entity;

import killercreepr.cruxentities.persistence.CruxEntitiesPersist;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class SimpleCruxMob implements CruxMob{
    protected final @NotNull Key key;
    public SimpleCruxMob(@NotNull Key key) {
        this.key = key;
    }

    @Override
    public final @NotNull Entity spawn(@NotNull Location at, @Nullable Consumer<Entity> consumer) {
        Entity e = spawnAt(at,  spawn ->{
            if(consumer != null) consumer.accept(spawn);
            CruxEntitiesPersist.ENTITY.set(spawn, key);
        });
        return e;
    }

    protected abstract @NotNull Entity spawnAt(@NotNull Location at, @Nullable Consumer<Entity> consumer);

    @Override
    public @NotNull Key key() {
        return key;
    }
}
