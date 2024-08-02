package killercreepr.cruxadvancements.config;

import killercreepr.cruxadvancements.advancement.criteria.CruxCriteria;
import killercreepr.cruxadvancements.advancement.progression.CruxAdvancementProgress;
import killercreepr.cruxadvancements.advancement.progression.ListAdvancementProgress;
import killercreepr.cruxadvancements.advancement.progression.NumberAdvancementProgress;
import killercreepr.cruxadvancements.advancement.progression.SimpleCriterionProgress;
import killercreepr.cruxadvancements.advancement.reward.CruxAdvanceReward;
import killercreepr.cruxadvancements.config.handler.FileCruxAdvanceReward;
import killercreepr.cruxadvancements.config.handler.FileCruxAdvancementProgress;
import killercreepr.cruxadvancements.config.handler.FileCruxCriteria;
import killercreepr.cruxadvancements.config.handler.FileSimpleCriterionProgress;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import org.jetbrains.annotations.NotNull;

public class CruxConfigHook {
    public static void registerHandlers(){
        registerHandlers(CfgRegistries.JSON);
        registerHandlers(CfgRegistries.YAML);
    }
    public static final FileCruxAdvancementProgress CRUX_ADVANCEMENT_PROGRESS = new FileCruxAdvancementProgress();
    public static void registerHandlers(@NotNull FileRegistry registry){
        registry.registerHandler(CruxCriteria.class, new FileCruxCriteria());
        registry.registerHandler(SimpleCriterionProgress.class, new FileSimpleCriterionProgress());
        registry.registerHandler(CruxAdvancementProgress.class, CRUX_ADVANCEMENT_PROGRESS);
        //register the inheritors objects so the config registry doesn't pick a random handler
        registry.registerHandler(ListAdvancementProgress.class, CRUX_ADVANCEMENT_PROGRESS);
        registry.registerHandler(NumberAdvancementProgress.class, CRUX_ADVANCEMENT_PROGRESS);
        registry.registerHandler(CruxAdvanceReward.class, new FileCruxAdvanceReward());
    }
}
