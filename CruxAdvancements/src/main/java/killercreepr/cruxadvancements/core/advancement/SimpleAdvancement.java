package killercreepr.cruxadvancements.core.advancement;

import killercreepr.cruxadvancements.api.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.api.advancement.criteria.CruxCriteria;
import killercreepr.cruxadvancements.api.advancement.icon.CruxAdvancementIcon;
import killercreepr.cruxadvancements.api.advancement.progress.CruxAdvancementProgress;
import killercreepr.cruxadvancements.api.advancement.reward.CruxAdvanceReward;
import killercreepr.cruxadvancements.core.advancement.criteria.ListCriteria;
import killercreepr.cruxadvancements.core.advancement.criteria.NumberCriteria;
import killercreepr.cruxadvancements.core.advancement.progress.ListAdvancementProgress;
import killercreepr.cruxadvancements.core.advancement.progress.NumberAdvancementProgress;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleAdvancement implements CruxAdvancement {
    protected final @NotNull Key key;
    protected final @Nullable Key parentKey;
    protected final @NotNull CruxAdvancementIcon icon;
    protected final @NotNull CruxCriteria criteria;
    protected final @Nullable CruxAdvanceReward reward;
    //we're using strings here because Minecraft stores progress as strings as well.
    //So... just in case if we'd ever want to store more than just UUIDs here in the future
    protected final @NotNull Map<String, CruxAdvancementProgress> progressMap = new ConcurrentHashMap<>();

    public SimpleAdvancement(@NotNull Key key, @Nullable Key parentKey, @NotNull CruxAdvancementIcon icon, @NotNull CruxCriteria criteria, @Nullable CruxAdvanceReward reward) {
        this.key = key;
        this.parentKey = parentKey;
        this.icon = icon;
        this.criteria = criteria;
        this.reward = reward;
    }

    @Override
    public @Nullable Key parent() {
        return parentKey;
    }

    @Override
    public @Nullable CruxAdvanceReward reward() {
        return reward;
    }

    @Override
    public @NotNull CruxAdvancementProgress getProgress(@NotNull UUID uuid) {
        return progressMap.computeIfAbsent(uuid.toString(), (u) -> buildProgress());
    }

    @Override
    public @Nullable CruxAdvancementProgress getProgressIfPresent(@NotNull UUID uuid) {
        return progressMap.get(uuid.toString());
    }

    @Override
    public @NotNull Map<String, CruxAdvancementProgress> getProgressMap() {
        return progressMap;
    }

    @Override
    public void setProgress(@NotNull UUID uuid, @Nullable CruxAdvancementProgress progress) {
        if(progress==null) progressMap.remove(uuid.toString());
        else progressMap.put(uuid.toString(), progress);
    }

    public @NotNull CruxAdvancementProgress buildProgress(){
        if(getCriteria() instanceof ListCriteria c){
            return new ListAdvancementProgress(c);
        }
        if(getCriteria() instanceof NumberCriteria c){
            return new NumberAdvancementProgress(c);
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
    public @NotNull CruxAdvancementIcon getIcon() {
        return icon;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
