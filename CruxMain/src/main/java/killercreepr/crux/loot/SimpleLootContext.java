package killercreepr.crux.loot;

import killercreepr.crux.loot.api.LootContext;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class SimpleLootContext implements LootContext {
    private final Location location;
    private final float luck;
    private final Entity looter;
    private final Entity looted;

    private final Random random;

    public SimpleLootContext(Location location, Entity looter, Entity looted, Random random){
        this(location,
            looter instanceof LivingEntity l && l.getAttribute(Attribute.GENERIC_LUCK) != null ?
                (float) l.getAttribute(Attribute.GENERIC_LUCK).getValue() : 0f,
            looter, looted, random);
    }

    public SimpleLootContext(Location location, float luck, Entity looter, Entity looted, Random random) {
        this.location = location;
        this.luck = luck;
        this.looter = looter;
        this.looted = looted;
        this.random = random;
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
    public @Nullable Entity getLooter() {
        return looter;
    }
    @Override
    public @Nullable Entity getLooted() {
        return looted;
    }

    public static final class Builder{
        private Location location;
        private float luck;
        private Entity looter;
        private Entity looted;
        private Random random;

        public Builder setRandom(Random random) {
            this.random = random; return this;
        }

        public Builder setLocation(Location location) {
            this.location = location;
            return this;
        }

        public Builder setLuck(float luck) {
            this.luck = luck; return this;
        }

        public Builder setLooter(Entity looter) {
            this.looter = looter; return this;
        }

        public Builder setLooted(Entity looted) {
            this.looted = looted; return this;
        }

        public @NotNull SimpleLootContext build(){
            return new SimpleLootContext(location, luck, looter, looted, random);
        }
    }
}
