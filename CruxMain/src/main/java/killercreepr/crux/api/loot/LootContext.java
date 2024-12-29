package killercreepr.crux.api.loot;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.core.loot.SimpleLootContext;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public interface LootContext {
    static Builder builder(){
        return new SimpleLootContext.Builder();
    }
    static LootContext empty(){
        return EMPTY;
    }
    LootContext EMPTY = builder().build();

    @NotNull Random getRandom();
    @Nullable Location getLocation();
    float getLuck();
    @Nullable Object getLooter();
    @Nullable Object getLooted();
    @NotNull
    DataExchange info();

    LootContext withRandom(@NotNull Random random);
    LootContext withLocation(@NotNull Location location);
    LootContext withLooter(@Nullable Object looter);
    LootContext withLooted(@Nullable Object looted);
    LootContext withInfo(@NotNull DataExchange info);

    interface Builder{
        Builder info(DataExchange info);

        Builder random(Random random);

        Builder location(Location location);

        Builder luck(Float luck);
        Builder looter(Object looter);

        Builder looted(Object looted);

        LootContext build();
    }
}
