package killercreepr.crux.api.world;

import org.bukkit.World;
/**
 * Represents the 8 phases of the Minecraft moon.
 * <p>
 * Cycle repeats every 8 Minecraft days (192,000 ticks).
 * <ul>
 *     <li>0 = FULL</li>
 *     <li>1 = WANING_GIBBOUS</li>
 *     <li>2 = LAST_QUARTER</li>
 *     <li>3 = WANING_CRESCENT</li>
 *     <li>4 = NEW</li>
 *     <li>5 = WAXING_CRESCENT</li>
 *     <li>6 = FIRST_QUARTER</li>
 *     <li>7 = WAXING_GIBBOUS</li>
 * </ul>
 */
public enum MoonPhase {
    FULL,
    WANING_GIBBOUS,
    LAST_QUARTER,
    WANING_CRESCENT,
    NEW,
    WAXING_CRESCENT,
    FIRST_QUARTER,
    WAXING_GIBBOUS;

    /**
     * Returns the fraction of the moon that is visible (0.0 – 1.0).
     */
    public double getVisibility() {
        return switch (this) {
            case FULL -> 1.0;
            case WANING_GIBBOUS -> 0.75;
            case LAST_QUARTER -> 0.5;
            case WANING_CRESCENT -> 0.25;
            case NEW -> 0.0;
            case WAXING_CRESCENT -> 0.25;
            case FIRST_QUARTER -> 0.5;
            case WAXING_GIBBOUS -> 0.75;
            default -> 0.0;
        };
    }

    public boolean isFullMoon() {
        return this == FULL;
    }

    public boolean isNewMoon() {
        return this == NEW;
    }

    public MoonPhase getRelativePhase(int offset) {
        int length = values().length;
        return values()[(this.ordinal() + offset % length + length) % length];
    }

    public MoonPhase getNextPhase() {
        return getRelativePhase(1);
    }

    public MoonPhase getPreviousPhase() {
        return getRelativePhase(-1);
    }

    public static MoonPhase getByID(int id) {
        if (id < 0 || id >= values().length) {
            throw new IllegalArgumentException("Invalid moon phase id: " + id);
        }
        return values()[id];
    }

    public static MoonPhase getByWorld(World world){
        return getByWorldFullTime(world.getFullTime());
    }

    public static MoonPhase getByWorldFullTime(long fullTime){
        int moonPhase = (int) ((fullTime / 24000L) % 8);
        return getByID(moonPhase);
    }
}
