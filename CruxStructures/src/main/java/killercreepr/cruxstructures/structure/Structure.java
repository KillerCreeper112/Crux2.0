package killercreepr.cruxstructures.structure;

import killercreepr.cruxstructures.event.StructurePlaceEvent;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface Structure {
    @NotNull
    StructurePlaceEvent place(@NotNull Location at);
}
