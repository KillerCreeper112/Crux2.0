package killercreepr.cruxadvancements.crazy;

import eu.endercentral.crazy_advancements.advancement.AdvancementFlag;
import killercreepr.cruxadvancements.advancement.SimpleAdvancement;
import killercreepr.cruxadvancements.advancement.criteria.CruxCriteria;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class CrazyAdvancement extends SimpleAdvancement {
    protected final @NotNull CrazyAdvancementDisplay display;
    protected final @NotNull AdvancementFlag[] flags;
    public CrazyAdvancement(@NotNull Key key, @Nullable Key parentKey, @NotNull CruxCriteria criteria, @NotNull CrazyAdvancementDisplay display, @NotNull AdvancementFlag[] flags) {
        super(key, parentKey, criteria);
        this.display = display;
        this.flags = flags;
    }

    public @NotNull CrazyAdvancementDisplay getDisplay() {
        return display;
    }

    public @NotNull AdvancementFlag[] getFlags() {
        return flags;
    }

    public void save(@NotNull File file){

    }

    public void load(@NotNull File file){

    }
}
