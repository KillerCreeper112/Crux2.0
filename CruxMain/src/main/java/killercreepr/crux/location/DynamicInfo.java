package killercreepr.crux.location;

import org.bukkit.Location;
import org.bukkit.util.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DynamicInfo {
    private final Consumer<Location> locationConsumer;
    public DynamicInfo(@Nullable Consumer<Location> consumer){
        this.locationConsumer = consumer;
    }

    public @NotNull Location apply(@NotNull Location l){
        if(locationConsumer != null) locationConsumer.accept(l);
        return l;
    }
}
