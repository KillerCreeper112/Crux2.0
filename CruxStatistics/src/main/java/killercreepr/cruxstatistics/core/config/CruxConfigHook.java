package killercreepr.cruxstatistics.core.config;

import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.cruxstatistics.api.statistic.CruxStatistic;
import killercreepr.cruxstatistics.core.config.handler.FileCruxStatistic;
import org.jetbrains.annotations.NotNull;

public class CruxConfigHook {
    public static void register(){
        CfgRegistries.SIMPLE_REGISTRY.forEach(CruxConfigHook::registerHandlers);
    }

    public static void registerHandlers(@NotNull FileRegistry registry){
        registry.registerFileHandler(CruxStatistic.class, new FileCruxStatistic());
    }
}
