package killercreepr.cruxadvancements.crazy.advancement;

import eu.endercentral.crazy_advancements.advancement.AdvancementFlag;
import killercreepr.crux.core.Crux;
import killercreepr.cruxadvancements.api.advancement.criteria.CruxCriteria;
import killercreepr.cruxadvancements.api.advancement.flag.CruxAdvancementFlag;
import killercreepr.cruxadvancements.api.advancement.objective.AdvancementObjective;
import killercreepr.cruxadvancements.api.advancement.reward.CruxAdvanceReward;
import killercreepr.cruxadvancements.core.advancement.objective.SimpleObjectiveAdvancement;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class CrazyAdvancement extends SimpleObjectiveAdvancement {
    protected final @NotNull CrazyAdvancementDisplay display;
    protected final @NotNull CruxAdvancementFlag[] flags;

    public CrazyAdvancement(@NotNull Key key,
                            @Nullable Key parentKey,
                            @NotNull CrazyAdvancementDisplay display,
                            @NotNull CruxCriteria criteria,
                            @Nullable CruxAdvanceReward reward, @NotNull CruxAdvancementFlag[] flags,
                            @NotNull Map<String, AdvancementObjective> objectives, int updateAdvancementPeriod) {
        super(key, parentKey, display, criteria, reward, objectives, updateAdvancementPeriod);
        this.display = display;
        this.flags = flags;
    }

    public @NotNull CrazyAdvancementDisplay getDisplay() {
        return display;
    }

    public @NotNull CruxAdvancementFlag[] getFlags() {
        return flags;
    }

    public AdvancementFlag[] toCrazyFlags(){
        List<AdvancementFlag> convert = new ArrayList<>();
        for(CruxAdvancementFlag flag : flags){
            if(flag == CruxAdvancementFlag.GLOBAL) continue;
            try{
                convert.add(AdvancementFlag.valueOf(flag.toString()));
            }catch (IllegalArgumentException ignored){
                Crux.log(Level.WARNING, "No CrazyAdvancementFlag of " + flag + "!");
            }
        }
        return convert.toArray(new AdvancementFlag[0]);
    }

    public boolean hasFlag(CruxAdvancementFlag flag){
        return Arrays.stream(getFlags()).anyMatch(d -> d == flag);
    }
}
