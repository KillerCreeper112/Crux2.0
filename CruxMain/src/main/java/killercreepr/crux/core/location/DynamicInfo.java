package killercreepr.crux.core.location;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class DynamicInfo {
    public static DynamicInfo info(@Nullable Consumer<Location> consumer){
        return new DynamicInfo(consumer);
    }

    private final Consumer<Location> locationConsumer;
    public DynamicInfo(@Nullable Consumer<Location> consumer){
        this.locationConsumer = consumer;
    }

    public @NotNull Location apply(@NotNull Location l){
        if(locationConsumer != null) locationConsumer.accept(l);
        return l;
    }
}
