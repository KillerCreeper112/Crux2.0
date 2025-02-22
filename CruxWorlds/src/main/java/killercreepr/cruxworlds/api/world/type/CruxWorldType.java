package killercreepr.cruxworlds.api.world.type;

import killercreepr.crux.api.data.CruxKeyed;
import killercreepr.cruxworlds.api.world.CruxWorld;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public interface CruxWorldType extends CruxKeyed {
    @NotNull
    CruxWorld generate(@NotNull Key name);
    @NotNull Key defaultWorldKey();
}
