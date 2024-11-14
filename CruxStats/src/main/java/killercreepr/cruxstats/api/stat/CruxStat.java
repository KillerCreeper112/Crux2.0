package killercreepr.cruxstats.api.stat;

import killercreepr.crux.valueproviders.number.NumberHolder;
import killercreepr.cruxstats.core.stat.SimpleCruxStatInstance;
import killercreepr.cruxstats.core.stat.SimpleStat;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CruxStat extends Keyed {
    static CruxStat stat(@NotNull Key key){
        return new SimpleStat(key);
    }

    static CruxStat stat(@NotNull Key key, @NotNull NumberHolder base){
        return new SimpleStat(key){
            @Override
            public @NotNull CruxStatInstance createNewInstance() {
                return new SimpleCruxStatInstance(this, base);
            }
        };
    }

    default double processValue(double value){
        return value;
    }
    default @Nullable CruxStatInstance createNewInstance(){
        return null;
    }
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
