package killercreepr.crux.loot.impl;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.Holder;
import killercreepr.crux.loot.LootContext;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Random;

public class SimpleLootContext implements LootContext {
    private final Location location;
    private final float luck;
    private final Object looter;
    private final Object looted;

    private final Random random;
    private final DataExchange info;

    public SimpleLootContext(Location location, Object looter, Object looted, Random random, DataExchange info){
        this(location,
            looter instanceof LivingEntity l && l.getAttribute(Attribute.GENERIC_LUCK) != null ?
                (float) l.getAttribute(Attribute.GENERIC_LUCK).getValue() : 0f,
            looter, looted, random, info);
    }

    public SimpleLootContext(Location location, float luck, Object looter, Object looted, Random random, DataExchange info) {
        this.location = location;
        this.luck = luck;
        this.looter = looter;
        this.looted = looted;
        this.random = random;
        this.info = info.append(Map.of(
            "location", Holder.direct(location),
            "luck", Holder.direct(luck),
            "looter", Holder.direct(looter),
            "looted", Holder.direct(looted),
            "random", Holder.direct(random)
        ));
    }
    @Override
    public @NotNull Random getRandom() {
        return random;
    }

    @Override
    public @Nullable Location getLocation() {
        return location;
    }
    @Override
    public float getLuck() {
        return luck;
    }
    @Override
    public @Nullable Object getLooter() {
        return looter;
    }
    @Override
    public @Nullable Object getLooted() {
        return looted;
    }

    @Override
    public @NotNull DataExchange info() {
        return info;
    }

    @Override
    public LootContext withRandom(@NotNull Random random) {
        return new SimpleLootContext(location, looter, looted, random, info);
    }

    @Override
    public LootContext withLocation(@NotNull Location location) {
        return new SimpleLootContext(location, looter, looted, random, info);
    }

    @Override
    public LootContext withLooter(@Nullable Object looter) {
        return new SimpleLootContext(location, looter, looted, random, info);
    }

    @Override
    public LootContext withLooted(@Nullable Object looted) {
        return new SimpleLootContext(location, looter, looted, random, info);
    }

    @Override
    public LootContext withInfo(@NotNull DataExchange info) {
        return new SimpleLootContext(location, looter, looted, random, info);
    }

    public static final class Builder implements LootContext.Builder{
        private Location location;
        private Float luck;
        private Object looter;
        private Object looted;
        private Random random;
        private DataExchange info;

        public Builder info(DataExchange info) {
            this.info = info; return this;
        }

        public Builder random(Random random) {
            this.random = random; return this;
        }

        public Builder location(Location location) {
            this.location = location;
            return this;
        }

        public Builder luck(Float luck) {
            this.luck = luck; return this;
        }

        public Builder looter(Object looter) {
            this.looter = looter; return this;
        }

        public Builder looted(Object looted) {
            this.looted = looted; return this;
        }

        public @NotNull SimpleLootContext build(){
            if(luck == null){
                luck = 0f;
                if(looter instanceof LivingEntity l){
                    AttributeInstance instance = l.getAttribute(Attribute.GENERIC_LUCK);
                    if(instance != null) luck = (float) instance.getValue();
                }
            }
            return new SimpleLootContext(location,
                luck,
                looter, looted,
                random == null ? new Random() : random,
                info == null ? DataExchange.empty() : info);
        }
    }
}
