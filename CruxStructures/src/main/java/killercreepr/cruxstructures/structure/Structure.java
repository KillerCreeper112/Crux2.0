package killercreepr.cruxstructures.structure;

import killercreepr.cruxstructures.event.StructurePlaceEvent;
import net.kyori.adventure.key.Keyed;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

public interface Structure extends Keyed {
    @NotNull
    StructurePlaceEvent place(@NotNull Location at);
    @NotNull
    BoundingBox boundingBox();

    default boolean isPersistent(){ return false; }
}
