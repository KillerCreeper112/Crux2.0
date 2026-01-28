package killercreepr.crux.core.loot;

import killercreepr.crux.api.component.DataComponentAccessor;
import killercreepr.crux.api.component.DataComponentHandler;
import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.LootCtxObjects;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

//todo this loot context thing needs to be worked out man
public class SimpleLootContext extends DataComponentAccessor.SimpleImmutableHandler implements LootContext {
    protected final DataExchange info;
    public SimpleLootContext(Map<DataComponentType<?>, Holder<?>> map, DataExchange info) {
        super(map);
        this.info = info;
    }

    public SimpleLootContext(Collection<TypedDataComponent<?>> data, DataExchange info) {
        super(data);
        this.info = info;
    }

    @Override
    public <T> LootContext with(DataComponentType<? extends T> type, T value) {
        return new SimpleLootContext(addAndCopy(type, value), info);
    }

    @Override
    public <T> LootContext with(DataComponentType<? extends T> type, Holder<T> value) {
        return new SimpleLootContext(addAndCopy(type, value), info);
    }

    @Override
    public @NotNull DataExchange info() {
        return info;
    }

    /*private final Location location;
            private final float luck;
            private final Object looter;
            private final Object looted;

            private final Random random;
            private final DataExchange info;

            public SimpleLootContext(Location location, Object looter, Object looted, Random random, DataExchange info){
                this(location,
                    looter instanceof LivingEntity l && l.getAttribute(Attribute.LUCK) != null ?
                        (float) l.getAttribute(Attribute.LUCK).getValue() : 0f,
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
            }*/
    @Override
    public @NotNull Random getRandom() {
        return getOrThrow(LootCtxObjects.RANDOM);
    }

    @Override
    public @Nullable Location getLocation() {
        return get(LootCtxObjects.LOCATION);
    }
    @Override
    public float getLuck() {
        return getOrDefault(LootCtxObjects.LUCK, 0f);
    }
    @Override
    public @Nullable Object getLooter() {
        return get(LootCtxObjects.LOOTER);
    }
    @Override
    public @Nullable Object getLooted() {
        return get(LootCtxObjects.LOOTED);
    }

    @Override
    public LootContext withInfo(@NotNull DataExchange info) {
        return new SimpleLootContext(map, info);
    }

    /* @Override
    public @NotNull DataExchange info() {
        return info;
    }*/

    /*@Override
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
    }*/

    public static final class Builder extends DataComponentHandler.Simple implements LootContext.Builder{
        /*private Location location;
        private Float luck;
        private Object looter;
        private Object looted;
        private Random random;
        private DataExchange info;*/
        private DataExchange info;

        /*public Builder info(DataExchange info) {

            this.info = info; return this;
        }*/

        @Override
        public DataExchange info() {
            return info;
        }

        @Override
        public LootContext.Builder info(DataExchange info) {
            this.info = info;
            return this;
        }

        public Builder random(Random random) {
            return add(LootCtxObjects.RANDOM, random);
        }

        public Builder location(Location location) {
            return add(LootCtxObjects.LOCATION, location);
        }

        public Builder luck(Float luck) {
            return add(LootCtxObjects.LUCK, luck);
        }

        public Builder looter(Object looter) {
            return add(LootCtxObjects.LOOTER, looter);
        }

        public Builder looted(Object looted) {
            return add(LootCtxObjects.LOOTED, looted);
        }

        @Override
        public <T> Builder add(DataComponentType<T> type, T value) {
            set(type, value);
            return this;
        }

        public @NotNull SimpleLootContext build(){
            if(!has(LootCtxObjects.LUCK)){
                if(get(LootCtxObjects.LOOTER) instanceof LivingEntity l){
                    AttributeInstance instance = l.getAttribute(Attribute.LUCK);
                    if(instance != null){
                        float luck = (float) instance.getValue();
                        set(LootCtxObjects.LUCK, luck);
                    }
                }
            }

            if(!has(LootCtxObjects.RANDOM)){
                set(LootCtxObjects.RANDOM, new Random());
            }

            if(info == null) info = DataExchange.empty();

            if(has(LootCtxObjects.LOOTED)){
                info = info.append("looted", Holder.direct(get(LootCtxObjects.LOOTED)));
            }
            if(has(LootCtxObjects.LOOTER)){
                info = info.append("looter", Holder.direct(get(LootCtxObjects.LOOTER)));
            }
            if(has(LootCtxObjects.LOCATION)){
                info = info.append("location", Holder.direct(get(LootCtxObjects.LOCATION)));
            }
            if(has(LootCtxObjects.LUCK)){
                info = info.append("luck", Holder.direct(get(LootCtxObjects.LUCK)));
            }
            if(has(LootCtxObjects.RANDOM)){
                info = info.append("random", Holder.direct(get(LootCtxObjects.RANDOM)));
            }

            return new SimpleLootContext(map, info);

            /*if(luck == null){
                luck = 0f;
                if(looter instanceof LivingEntity l){
                    AttributeInstance instance = l.getAttribute(Attribute.LUCK);
                    if(instance != null) luck = (float) instance.getValue();
                }
            }
            return new SimpleLootContext(location,
                luck,
                looter, looted,
                random == null ? new Random() : random,
                info == null ? DataExchange.empty() : info);*/
        }
    }
}
