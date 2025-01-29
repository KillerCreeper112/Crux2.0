package killercreepr.cruxentities.api.entity.mob.goal;

import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

public interface LocationTargetMobGoal {
    @Nullable Location getTargetLocation();
    void setTargetLocation(@Nullable Location location);
}
