package killercreepr.cruxpotions.api.potion.inflictor;

import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

public interface LocationedInflictor extends PotionInflictor{
    @Nullable Location getLocation();
}
