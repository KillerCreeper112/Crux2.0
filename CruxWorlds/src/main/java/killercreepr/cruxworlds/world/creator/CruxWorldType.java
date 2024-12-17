package killercreepr.cruxworlds.world.creator;

import killercreepr.cruxworlds.world.CruxWorld;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;

public interface CruxWorldType extends Keyed {
    @NotNull
    CruxWorld generate(@NotNull String name);
}
