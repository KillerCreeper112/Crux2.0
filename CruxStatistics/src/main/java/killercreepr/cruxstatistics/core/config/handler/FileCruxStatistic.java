package killercreepr.cruxstatistics.core.config.handler;

import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FilePrimitive;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxstatistics.api.statistic.CruxStatistic;
import killercreepr.cruxstatistics.api.statistic.CruxStatisticKeyedType;
import killercreepr.cruxstatistics.api.statistic.CruxStatisticType;
import killercreepr.cruxstatistics.core.registries.CruxStatisticRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;


public class FileCruxStatistic implements FileObjectHandler<CruxStatistic<?>> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull CruxStatistic<?> o) {
        return new FilePrimitive(o.getName());
    }

    @Override
    public @Nullable CruxStatistic<?> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FilePrimitive p)) return null;
        String id = p.getAsString();
        String[] split = id.split(" ");
        Key statKey = Crux.key(split[0]);
        CruxStatisticType<?> type = CruxStatisticRegistries.STATISTIC_TYPE.get(statKey);
        if(type == null){
            Crux.log(Level.SEVERE, "CruxStatisticType " + statKey + " not found!");
            return null;
        }

        if(split.length > 1){
            if(!(type instanceof CruxStatisticKeyedType<?> typed)){
                Crux.log(Level.SEVERE, type + " CruxStatisticType is not a KeyedType! " + type.key());
                return null;
            }
            Object value = typed.getRegistry().get(Crux.key(split[1]));
            if(value == null){
                Crux.log(Level.SEVERE, type + " CruxStatisticType value of " + split[1] + " not found!");
                return null;
            }
            return CruxStatistic.statisticKeyedUnchecked(typed, value);
        }

        return CruxStatistic.statistic(type);
    }
}
