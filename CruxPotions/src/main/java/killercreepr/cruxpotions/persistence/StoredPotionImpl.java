package killercreepr.cruxpotions.persistence;

import killercreepr.cruxpotions.potions.ActivePotion;
import killercreepr.cruxpotions.potions.CruxPotion;
import killercreepr.cruxpotions.potions.inflictor.PotionInflictor;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StoredPotionImpl implements StoredPotion {
    private final CruxPotion potion;
    private final int duration;
    private final int amplifier;

    public StoredPotionImpl(@NotNull CruxPotion potion, int duration, int amplifier) {
        this.potion = potion;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    public StoredPotionImpl(@NotNull ActivePotion active){
        potion = active.getPotion();
        duration = active.getDuration();
        amplifier = active.getAmplifier();
    }
    @Override
    public @NotNull ActivePotion create(@NotNull Entity e){
        return potion.create(e, duration, amplifier);
    }
    @Override
    public @NotNull ActivePotion create(@NotNull Entity e, @Nullable PotionInflictor inflictor){
        return potion.create(e, duration, amplifier, inflictor);
    }
    @Override
    public @NotNull CruxPotion getPotion() {
        return potion;
    }

    @Override
    public int getDuration() {
        return duration;
    }
    @Override
    public int getAmplifier() {
        return amplifier;
    }

    @Override
    public @NotNull StoredPotion withDuration(int duration) {
        return new StoredPotionImpl(potion, duration, amplifier);
    }

    @Override
    public @NotNull StoredPotion withAmplifier(int amplifier) {
        return new StoredPotionImpl(potion, duration, amplifier);
    }

    @Override
    public @NotNull StoredPotion withPotion(@NotNull CruxPotion potion) {
        return new StoredPotionImpl(potion, duration, amplifier);
    }
}
