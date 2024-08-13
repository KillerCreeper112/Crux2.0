package killercreepr.crux.loot.api;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public interface LootContext {
    @NotNull Random getRandom();
    @Nullable Location getLocation();
    float getLuck();
    @Nullable Entity getLooter();
    @Nullable Entity getLooted();
}
