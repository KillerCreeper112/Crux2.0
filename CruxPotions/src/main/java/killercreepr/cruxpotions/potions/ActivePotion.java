package killercreepr.cruxpotions.potions;

import io.netty.util.internal.UnstableApi;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxpotions.persistence.StoredPotion;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ActivePotion{
    static @NotNull String formatPotion(@NotNull StoredPotion stored){
        return formatPotion(stored.getPotion(), stored.getAmplifier(), stored.getDuration());
    }

    static @NotNull String formatPotion(@NotNull CruxPotion pot, int amplifier, int duration){
        return formatPotion(pot.getName(), amplifier, duration);
    }

    @UnstableApi
    static @NotNull String formatPotion(@NotNull String name, int amplifier, int duration){
        StringBuilder builder = new StringBuilder(name).append(" (");
        if(amplifier > 0){
            builder.append(CruxMath.numeral(amplifier+1)).append(" ");
        }
        builder.append(formatDuration(duration)).append(")");
        return builder.toString();
    }

    static @NotNull String formatDuration(int duration){
        if(duration == -1) return "∞";

        int seconds = duration / 20;
        int minutes = seconds / 60;
        seconds -= minutes * 60;
        return minutes + ":" + CruxMath.padWithZeroIfSingleDigit(seconds);
    }
    default int compareDuration(@NotNull ActivePotion check){
        int duration = getDuration();
        if(duration == check.getDuration()) return 0;
        if(hasInfiniteDuration()) return 1;
        if(check.hasInfiniteDuration()) return -1;
        return Integer.compare(duration, check.getDuration());
    }

    /**
     * @return Whether this effect is infinite.
     */
    default boolean hasInfiniteDuration(){ return getDuration() == -1; }

    /**
     * @return Whether to remove this potion from the entity.
     */
    boolean tick();
    /**
     * @param newDuration The new duration (in ticks) that this active effect should have.
     * @param newAmplifier The new amplifier that this active effect should have.
     */
    void update(int newDuration, int newAmplifier);

    default ActivePotion setDuration(int duration) {
        return setDuration(duration, false);
    }

    default ActivePotion setAmplifier(int amplifier) {
        return setAmplifier(amplifier, false);
    }

    /**
     *
     * @param duration The new effect's durations.
     * @param silent If this is false, the potion's update function will not be called.
     * @return The same active potion, for chaining.
     */
    ActivePotion setDuration(int duration, boolean silent);

    /**
     *
     * @param amplifier The new effect's amplifier.
     * @param silent If this is true, the potion's update function will not be called.
     * @return The same active potion, for chaining.
     */
    ActivePotion setAmplifier(int amplifier, boolean silent);
    void start();
    void stop();
    @NotNull CruxPotion getPotion();
    @NotNull Entity getEntity();
    int getDuration();
    int getAmplifier();

    @Nullable Component format();
}
