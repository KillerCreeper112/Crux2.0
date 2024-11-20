package killercreepr.cruxentities.entity;

import killercreepr.crux.api.entity.CruxEntitySnapshot;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CruxMobSnapshot implements CruxEntitySnapshot {
    protected final @NotNull CruxMob mob;
    public CruxMobSnapshot(@NotNull CruxMob mob) {
        this.mob = mob;
    }

    @Override
    public @NotNull Entity createEntity(@NotNull Location to, @Nullable Consumer<Entity> consumer) {
        return mob.spawn(to, consumer);
    }
}
