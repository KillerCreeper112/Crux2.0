package killercreepr.cruxadvancements.config;

import killercreepr.cruxadvancements.advancement.criteria.CruxCriteria;
import killercreepr.cruxadvancements.advancement.progression.SimpleCriterionProgress;
import killercreepr.cruxadvancements.config.handler.FileCruxCriteria;
import killercreepr.cruxadvancements.config.handler.FileSimpleCriterionProgress;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import org.jetbrains.annotations.NotNull;

public class CruxConfigHook {
    public static void register(){

    }
    public static void registerHandlers(){
        registerHandlers(CfgRegistries.JSON);
        registerHandlers(CfgRegistries.YAML);
    }
    public static void registerHandlers(@NotNull FileRegistry registry){
        registry.registerHandler(CruxCriteria.class, new FileCruxCriteria());
        registry.registerHandler(SimpleCriterionProgress.class, new FileSimpleCriterionProgress());
    }
}
