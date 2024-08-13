package killercreepr.crux.loot;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.Holder;
import killercreepr.crux.loot.api.LootContext;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Random;

public class SimpleLootContext implements LootContext {
    public static Builder builder(){
        return new Builder();
    }

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

    public static final class Builder{
        private Location location;
        private Float luck;
        private Object looter;
        private Object looted;
        private Random random;
        private DataExchange info;

        public Builder setInfo(DataExchange info) {
            this.info = info; return this;
        }

        public Builder setRandom(Random random) {
            this.random = random; return this;
        }

        public Builder setLocation(Location location) {
            this.location = location;
            return this;
        }

        public Builder setLuck(Float luck) {
            this.luck = luck; return this;
        }

        public Builder setLooter(Object looter) {
            this.looter = looter; return this;
        }

        public Builder setLooted(Object looted) {
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
