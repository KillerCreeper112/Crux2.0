package killercreepr.crux.util;

import org.jetbrains.annotations.Nullable;

import java.time.Duration;

public class CruxDuration {
    public static Duration ofTicks(long ticks){
        return Duration.ofMillis(50L * ticks);
    }

    public static long toTicks(@Nullable Duration duration){
        if(duration == null) return -1L;
        return duration.toMillis() / 50;
    }
}
