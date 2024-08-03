package killercreepr.cruxadvancements.crazy;

import eu.endercentral.crazy_advancements.advancement.AdvancementFlag;
import killercreepr.cruxadvancements.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.advancement.criteria.CruxCriteria;
import killercreepr.cruxadvancements.advancement.objective.AdvancementObjective;
import killercreepr.cruxadvancements.advancement.reward.CruxAdvanceReward;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CrazyAdvancement extends ObjectiveAdvancement {
    protected final @NotNull CrazyAdvancementDisplay display;
    protected final @NotNull AdvancementFlag[] flags;

    public CrazyAdvancement(@NotNull Key key,
                            @Nullable Key parentKey, @NotNull CruxCriteria criteria,
                            @Nullable CruxAdvanceReward reward,
                            @NotNull CrazyAdvancementDisplay display, @NotNull AdvancementFlag[] flags,
                            @NotNull Map<String, AdvancementObjective> objectives) {
        super(key, parentKey, criteria, reward, objectives);
        this.display = display;
        this.flags = flags;
    }

    public @NotNull CrazyAdvancementDisplay getDisplay() {
        return display;
    }

    public @NotNull AdvancementFlag[] getFlags() {
        return flags;
    }
}
