package killercreepr.crux.api.loot;

import killercreepr.crux.api.component.DataComponentAccessor;
import killercreepr.crux.api.component.DataComponentHandler;
import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.data.Holder;
import killercreepr.crux.core.loot.SimpleLootContext;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public interface LootContext extends DataComponentAccessor.ImmutableHandler {
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

    @Override
    <T> LootContext with(DataComponentType<? extends T> type, T value);
    @Override
    <T> LootContext with(DataComponentType<? extends T> type, Holder<T> value);

    @NotNull
    DataExchange info();

    /*LootContext withRandom(@NotNull Random random);
    LootContext withLocation(@NotNull Location location);
    LootContext withLooter(@Nullable Object looter);
    LootContext withLooted(@Nullable Object looted);*/
    LootContext withInfo(@NotNull DataExchange info);

    interface Builder extends DataComponentHandler {
        Builder info(DataExchange info);

        Builder random(Random random);

        Builder location(Location location);

        Builder luck(Float luck);
        Builder looter(Object looter);

        Builder looted(Object looted);
        <T> Builder add(DataComponentType<T> type, T value);

        LootContext build();
    }
}
