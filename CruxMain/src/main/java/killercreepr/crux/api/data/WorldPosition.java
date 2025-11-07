package killercreepr.crux.api.data;

import killercreepr.crux.api.math.CruxPosition;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public interface WorldPosition extends CruxPosition {
    @NotNull Key worldKey();
}
