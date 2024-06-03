package killercreepr.cruxpotions.persistence;

import killercreepr.cruxpotions.potions.ActivePotion;
import killercreepr.cruxpotions.potions.CruxPotion;
import killercreepr.cruxpotions.potions.inflictor.PotionInflictor;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomPotionHolder {
    private final CruxPotion potion;
    private final int duration;
    private final int amplifier;

    public CustomPotionHolder(@NotNull CruxPotion potion, int duration, int amplifier) {
        this.potion = potion;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    public CustomPotionHolder(@NotNull ActivePotion active){
        potion = active.getPotion();
        duration = active.getDuration();
        amplifier = active.getAmplifier();
    }

    public @NotNull ActivePotion create(@NotNull Entity e){
        return potion.create(e, duration, amplifier);
    }

    public @NotNull ActivePotion create(@NotNull Entity e, @Nullable PotionInflictor inflictor){
        return potion.create(e, duration, amplifier, inflictor);
    }

    public @NotNull CruxPotion getPotion() {
        return potion;
    }

    public int getDuration() {
        return duration;
    }

    public int getAmplifier() {
        return amplifier;
    }
}
