package killercreepr.cruxblocks.core.config.advancement;

import killercreepr.crux.core.Crux;
import killercreepr.cruxadvancements.api.advancement.objective.AdvancementObjective;
import killercreepr.cruxadvancements.core.advancement.objective.ObjectiveCommonData;
import killercreepr.cruxadvancements.core.config.CruxAdvanceCfgData;
import killercreepr.cruxadvancements.core.config.handler.FileAdvancementObjective;
import killercreepr.cruxadvancements.core.config.handler.FileSimpleAdvanceObjective;
import killercreepr.cruxblocks.core.advancement.objective.CruxBreakBlockObjective;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxBlocksAdvanceCfgHook {
    private static final FileAdvancementObjective fileAdvancementObjective = CruxAdvanceCfgData.fileAdvancementObjective();
    public static void load() {
        fileAdvancementObjective.registerCustomHandler(new FileSimpleAdvanceObjective<>(Crux.key("crux_break_block")) {
            @Override
            public @Nullable AdvancementObjective deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull ObjectiveCommonData data) {
                Integer maxProgress = e.getObject(Integer.class, "amount");
                if(maxProgress==null) maxProgress = 1;
                return new CruxBreakBlockObjective(data, maxProgress);
            }
        });
    }
}
