package killercreepr.crux.api.world;

import org.bukkit.World;

public enum MoonPhase {
    FULL,
    WANING_GIBBOUS,
    LAST_QUARTER,
    WANING_CRESCENT,
    NEW,
    WAXING_CRESCENT,
    FIRST_QUARTER,
    WAXING_GIBBOUS;

    public static MoonPhase getByID(int id){
        return MoonPhase.values()[id];
    }

    public static MoonPhase getByWorld(World world){
        return getByWorldFullTime(world.getFullTime());
    }

    public static MoonPhase getByWorldFullTime(long fullTime){
        int moonPhase = (int) ((fullTime / 24000L) % 8);
        return getByID(moonPhase);
    }
}
