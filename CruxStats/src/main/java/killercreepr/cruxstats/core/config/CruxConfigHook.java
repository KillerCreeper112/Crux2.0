package killercreepr.cruxstats.core.config;

import killercreepr.cruxconfig.config.bukkit.handler.impl.FileGenericEnum;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.cruxstats.api.stat.CruxStat;
import killercreepr.cruxstats.api.stat.CruxStatInstance;
import killercreepr.cruxstats.api.stat.CruxStatModifier;
import killercreepr.cruxstats.core.config.handler.FileStatInstance;
import killercreepr.cruxstats.core.config.handler.FileStatModifier;
import org.jetbrains.annotations.NotNull;

public class CruxConfigHook {
    public static void register(){
        CfgRegistries.SIMPLE_REGISTRY.forEach(CruxConfigHook::registerHandlers);
    }

    public static void registerHandlers(@NotNull FileRegistry registry){
        registry.registerFileHandler(CruxStat.Operation.class, new FileGenericEnum<>(CruxStat.Operation.class));
        registry.registerFileHandler(CruxStatInstance.class, new FileStatInstance());
        registry.registerFileHandler(CruxStatModifier.class, new FileStatModifier());
    }
}
