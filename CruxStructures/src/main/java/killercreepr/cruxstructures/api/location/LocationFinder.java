package killercreepr.cruxstructures.api.location;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LocationFinder {
    @Nullable
    Location find(@NotNull Location point);

    final class Dummy implements LocationFinder{
        @Override
        public @NotNull Location find(@NotNull Location point) {
            return point;
        }
    }
}
