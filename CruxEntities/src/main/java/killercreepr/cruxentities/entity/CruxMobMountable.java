package killercreepr.cruxentities.entity;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface CruxMobMountable {
    boolean canMount(@NotNull Entity mob, @NotNull Entity vehicle);
}
