package killercreepr.crux.core.util;

import killercreepr.crux.api.loot.ConditionedObject;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.WeightedObject;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.util.CruxWeightedSupplier;
import killercreepr.crux.api.util.CruxWeightedSupplierBuilder;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class SimpleWeightedSupplier<T> implements CruxWeightedSupplier<T> {
    protected final @NotNull Collection<T> poll;
    protected final int rolls;
    protected final float luck;
    protected final @Nullable Consumer<T> onAccepted;
    protected final @Nullable Predicate<T> filter;
    protected final @NotNull Function<T, Integer> weightFunction;
    protected final @NotNull Function<T, Float> qualityFunction;
    protected final Random random;
    protected final boolean allowDuplicates;
    public SimpleWeightedSupplier(@NotNull Collection<T> poll, int rolls, float luck, @Nullable Consumer<T> onAccepted, @Nullable Predicate<T> filter, @NotNull Function<T, Integer> weightFunction, @NotNull Function<T, Float> qualityFunction, Random random, boolean allowDuplicates) {
        this.poll = poll;
        this.rolls = rolls;
        this.luck = luck;
        this.onAccepted = onAccepted;
        this.filter = filter;
        this.weightFunction = weightFunction;
        this.qualityFunction = qualityFunction;
        this.random = random;
        this.allowDuplicates = allowDuplicates;
    }

    @Override
    public @NotNull List<T> rollList(){
        return rollList(filter);
    }
    @Override
    public @NotNull List<T> rollList(@Nullable Predicate<T> filter){
        return rollList(random, filter);
    }

    @Override
    public @NotNull List<T> rollList(@NotNull Random random) {
        return rollList(random, filter);
    }

    @Override
    public @NotNull List<T> rollList(boolean allowDuplicates) {
        return rollList(random, filter, allowDuplicates);
    }

    @Override
    public @NotNull List<T> rollList(@NotNull Random random, @Nullable Predicate<T> filter) {
        List<T> list = new ArrayList<>();
        roll(random, list::add, filter);
        return list;
    }

    @Override
    public @NotNull List<T> rollList(@NotNull Random random, @Nullable Predicate<T> filter, boolean allowDuplicates) {
        List<T> list = new ArrayList<>();
        roll(random, list::add, filter, allowDuplicates);
        return list;
    }

    @Override
    public @NotNull Map<T, Integer> roll(){
        return roll(filter);
    }

    @Override
    public @NotNull Map<T, Integer> roll(@NotNull Random random) {
        return roll(random, filter);
    }

    @Override
    public @NotNull Map<T, Integer> roll(boolean allowDuplicates) {
        Objects.requireNonNull(onAccepted, "onAccepted must not be null!");
        return roll(random, onAccepted, filter, allowDuplicates);
    }

    @Override
    public @NotNull Map<T, Integer> roll(@Nullable Predicate<T> filter){
        Objects.requireNonNull(onAccepted, "onAccepted must not be null!");
        return roll(onAccepted, filter);
    }

    @Override
    public @NotNull Map<T, Integer> roll(@NotNull Random random, @Nullable Predicate<T> filter) {
        Objects.requireNonNull(onAccepted, "onAccepted must not be null!");
        return roll(random, onAccepted, filter);
    }

    @Override
    public @NotNull Map<T, Integer> roll(@NotNull Consumer<T> onAccepted, @Nullable Predicate<T> filter) {
        return roll(random, onAccepted, filter);
    }

    @Override
    public @NotNull Map<T, Integer> roll(@NotNull Random random, @NotNull Consumer<T> onAccepted, @Nullable Predicate<T> filter){
        return roll(random, onAccepted, filter, allowDuplicates);
    }

    @Override
    public @NotNull Map<T, Integer> roll(
        @NotNull Random random,
        @NotNull Consumer<T> onAccepted,
        @Nullable Predicate<T> filter,
        boolean allowDuplicates
    ) {
        if (poll.isEmpty()) return Map.of();

        LinkedHashMap<T, Integer> data = new LinkedHashMap<>();
        for (T p : poll) {
            data.put(p, weightFunction.apply(p));
        }

        for (int i = 0; i < rolls; i++) {
            int totalWeight = 0;
            int weight;
            float quality;

            // Build a temporary pool that respects filters and updates weights
            Map<T, Integer> tempPool = new LinkedHashMap<>();
            for (T item : data.keySet()) {
                if (filter != null && !filter.test(item)) {
                    continue;
                }

                weight = weightFunction.apply(item);
                quality = qualityFunction.apply(item);
                weight += (int) (quality * luck);

                if (weight <= 0) continue;

                totalWeight += weight;
                tempPool.put(item, weight);
            }

            if (tempPool.isEmpty()) break;

            int chance = CruxMath.random(0, totalWeight, random);
            for (Map.Entry<T, Integer> entry : tempPool.entrySet()) {
                if (chance <= entry.getValue()) {
                    onAccepted.accept(entry.getKey());
                    if (!allowDuplicates) {
                        data.remove(entry.getKey());
                    }
                    break;
                }
                chance -= entry.getValue();
            }

            if (!allowDuplicates && data.isEmpty()) break;
        }

        return data;
    }


    public static final class Builder<T> implements CruxWeightedSupplierBuilder<T> {
        private @NotNull Collection<T> pool;
        private int rolls;
        private float luck;
        private Random random;
        private @Nullable Consumer<T> onAccepted;
        private @Nullable Predicate<T> filter;
        private Function<T, Integer> weightFunction;
        private Function<T, Float> qualityFunction;
        private boolean allowDuplicates;

        public Builder(@NotNull Collection<T> pool) {
            this.pool = pool;
        }

        @Override
        public Builder<T> luck(@NotNull LootContext context){
            float entityChance = 0f;

            if(context.getLooter() instanceof LivingEntity e){
                if(e.hasPotionEffect(PotionEffectType.LUCK)){
                    entityChance *= (1f + (.1f * (e.getPotionEffect(PotionEffectType.LUCK).getAmplifier() + 1)));
                }
                if(e.hasPotionEffect(PotionEffectType.UNLUCK)){
                    entityChance *= (1f - (.1f * (e.getPotionEffect(PotionEffectType.UNLUCK).getAmplifier() + 1)));
                }
            }
            entityChance /= 4f;
            return luck(entityChance);
        }
        @Override
        public Builder<T> filter(@NotNull LootContext context){
            return filter(item ->{
                if(!(item instanceof ConditionedObject o)) return true;
                boolean tested = true;
                for(LootCondition c : o.getConditions()){
                    tested = c.test(context);
                }
                return tested;
            });
        }
        @Override
        public Builder<T> pool(@NotNull Collection<T> pool) {
            this.pool = pool;
            return this;
        }
        @Override
        public Builder<T> rolls(int rolls) {
            this.rolls = rolls;
            return this;
        }
        @Override
        public Builder<T> allowDuplicates(boolean allowDuplicates) {
            this.allowDuplicates = allowDuplicates;
            return this;
        }

        @Override
        public CruxWeightedSupplierBuilder<T> random(Random random) {
            this.random = random;
            return this;
        }

        @Override
        public Builder<T> luck(float luck) {
            this.luck = luck;
            return this;
        }
        @Override
        public Builder<T> onAccepted(@Nullable Consumer<T> onAccepted) {
            this.onAccepted = onAccepted;
            return this;
        }
        @Override
        public Builder<T> filter(@Nullable Predicate<T> filter) {
            this.filter = filter;
            return this;
        }

        @Override
        public CruxWeightedSupplierBuilder<T> weightFunction(@Nullable Function<T, Integer> filter) {
            this.weightFunction = filter;
            return this;
        }

        @Override
        public CruxWeightedSupplierBuilder<T> qualityFunction(@Nullable Function<T, Float> filter) {
            this.qualityFunction = filter;
            return this;
        }

        @Override
        public @NotNull SimpleWeightedSupplier<T> build() {
            if(weightFunction == null){
                weightFunction = object -> ((WeightedObject) object).getWeight();
            }
            if(qualityFunction == null){
                qualityFunction = object -> ((WeightedObject) object).getQuality();
            }
            return new SimpleWeightedSupplier<>(
                pool, rolls, luck, onAccepted, filter,
                weightFunction, qualityFunction, random == null ? CruxMath.random() : random,
                allowDuplicates
            );
        }
    }
}
