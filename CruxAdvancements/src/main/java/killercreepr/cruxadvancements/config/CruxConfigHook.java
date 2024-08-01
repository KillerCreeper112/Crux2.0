package killercreepr.cruxadvancements.config;

import killercreepr.cruxadvancements.advancement.criteria.CruxCriteria;
import killercreepr.cruxadvancements.config.handler.FileCruxCriteria;
import killercreepr.cruxconfig.config.common.FileRegistry;
import org.jetbrains.annotations.NotNull;

public class CruxConfigHook {
    public static void register(){

    }
    public static void registerHandlers(@NotNull FileRegistry registry){
        registry.registerHandler(CruxCriteria.class, new FileCruxCriteria());
    }
}
