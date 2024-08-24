package killercreepr.crux.loot;

import killercreepr.crux.data.DataExchange;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public interface LootContext {
    @NotNull Random getRandom();
    @Nullable Location getLocation();
    float getLuck();
    @Nullable Object getLooter();
    @Nullable Object getLooted();
    @NotNull
    DataExchange info();
}
