package killercreepr.cruxworlds.world.creator;

import killercreepr.cruxworlds.world.CruxWorld;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface CruxWorldCreator {
    @NotNull
    CruxWorld create(@NotNull World world, @NotNull Collection<CruxWorldModuleCreator> moduleCreators);
}
