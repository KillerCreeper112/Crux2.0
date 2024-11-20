package killercreepr.cruxadvancements.core.config.handler;

import killercreepr.cruxadvancements.api.advancement.reward.CruxAdvanceReward;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomFileCruxAdvanceReward<T extends CruxAdvanceReward> {
    @NotNull String getType();
    @Nullable
    T deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e);
}
