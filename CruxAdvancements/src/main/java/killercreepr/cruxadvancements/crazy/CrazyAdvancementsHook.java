package killercreepr.cruxadvancements.crazy;

import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay;
import eu.endercentral.crazy_advancements.advancement.AdvancementFlag;
import eu.endercentral.crazy_advancements.advancement.AdvancementVisibility;
import killercreepr.cruxadvancements.config.handler.crazy.FileAdvancementVisibility;
import killercreepr.cruxadvancements.config.handler.crazy.FileCrazyAdvancement;
import killercreepr.cruxadvancements.config.handler.crazy.FileCrazyAdvancementDisplay;
import killercreepr.cruxconfig.config.bukkit.handler.impl.FileGenericEnum;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import org.jetbrains.annotations.NotNull;

public class CrazyAdvancementsHook {
    public static void registerHandlers(){
        registerHandlers(CfgRegistries.JSON);
        registerHandlers(CfgRegistries.YAML);
    }
    public static void registerHandlers(@NotNull FileRegistry registry){
        registry.registerFileHandler(AdvancementDisplay.AdvancementFrame.class, new FileGenericEnum<>(AdvancementDisplay.AdvancementFrame.class));
        registry.registerFileHandler(AdvancementFlag.class, new FileGenericEnum<>(AdvancementFlag.class));
        registry.registerFileHandler(AdvancementVisibility.class, new FileAdvancementVisibility());
        registry.registerFileHandler(CrazyAdvancementDisplay.class, new FileCrazyAdvancementDisplay());
        registry.registerFileHandler(CrazyAdvancement.class, new FileCrazyAdvancement());
    }
}
