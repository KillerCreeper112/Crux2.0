package killercreepr.cruxpotions.core.potions;

import killercreepr.cruxpotions.api.potion.CruxPotion;
import killercreepr.cruxpotions.api.potion.inflictor.PotionInflictor;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class InflictedPotion extends SimpleActivePotion {
    protected PotionInflictor inflictor;
    public InflictedPotion(@NotNull CruxPotion potion, @NotNull Entity entity, int duration, int amplifier) {
        super(potion, entity, duration, amplifier);
    }

    public InflictedPotion(@NotNull CruxPotion potion, @NotNull Entity entity, int duration, int amplifier,
                           @Nullable PotionInflictor inflictor) {
        super(potion, entity, duration, amplifier);
        this.inflictor = inflictor;
    }

    public @Nullable PotionInflictor getInflictor() {
        return inflictor;
    }

    public InflictedPotion setInflictor(@Nullable PotionInflictor inflictor) {
        this.inflictor = inflictor;
        return this;
    }
}
