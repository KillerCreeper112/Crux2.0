package killercreepr.cruxstats.api.stat;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxstats.core.stat.SimpleStat;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CruxStat extends Keyed {
    static CruxStat stat(@NotNull Key key){
        return stat(key, NumberProvider.zero());
    }

    static CruxStat stat(@NotNull Key key, @NotNull NumberProvider base){
        return new SimpleStat(key, base);
    }
    @NotNull NumberProvider baseValue();

    default double processValue(double value){
        return value;
    }
    @Nullable CruxStatInstance createNewInstance();
    enum Operation{
        ADD,
        MULTIPLY,
        SET;

        public static @Nullable Operation match(@NotNull String id){
            try{
                return Operation.valueOf(id.toUpperCase());
            }catch (IllegalArgumentException ignored){ return null; }
        }
    }
}
