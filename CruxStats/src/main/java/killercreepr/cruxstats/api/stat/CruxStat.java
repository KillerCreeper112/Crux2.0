package killercreepr.cruxstats.api.stat;

import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CruxStat extends Keyed {
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
