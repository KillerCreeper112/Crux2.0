package killercreepr.cruxadvancements.advancement;

import killercreepr.cruxadvancements.advancement.criteria.CruxCriteria;
import killercreepr.cruxadvancements.advancement.criteria.ListCriteria;
import killercreepr.cruxadvancements.advancement.criteria.NumberCriteria;
import killercreepr.cruxadvancements.advancement.progression.CruxAdvancementProgress;
import killercreepr.cruxadvancements.advancement.progression.ListAdvancementProgress;
import killercreepr.cruxadvancements.advancement.progression.NumberAdvancementProgress;
import killercreepr.cruxadvancements.advancement.reward.CruxAdvanceReward;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SimpleAdvancement implements CruxAdvancement {
    protected final @NotNull Key key;
    protected final @Nullable Key parentKey;
    protected final @NotNull CruxCriteria criteria;
    protected final @NotNull Map<String, CruxAdvancementProgress> progressMap = new HashMap<>();

    public SimpleAdvancement(@NotNull Key key, @Nullable Key parentKey, @NotNull CruxCriteria criteria) {
        this.key = key;
        this.parentKey = parentKey;
        this.criteria = criteria;
    }

    @Override
    public @Nullable Key parent() {
        return parentKey;
    }

    @Override
    public @Nullable CruxAdvanceReward reward() {
        return null;
    }

    @Override
    public @NotNull CruxAdvancementProgress getProgress(@NotNull UUID uuid) {
        return progressMap.computeIfAbsent(uuid.toString(), (u) -> buildProgress());
    }

    public @NotNull CruxAdvancementProgress buildProgress(){
        if(getCriteria() instanceof ListCriteria c){
            return new ListAdvancementProgress(c.getRequirements());
        }
        if(getCriteria() instanceof NumberCriteria c){
            return new NumberAdvancementProgress(c.getMaxProgress());
        }
        throw new UnsupportedOperationException(getCriteria() + " is an unsupported criteria!");
    }

    @Override
    public @NotNull CruxCriteria getCriteria() {
        return criteria;
    }

    @Override
    public boolean isGranted(@NotNull UUID uuid) {
        return getProgress(uuid).isDone();
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
