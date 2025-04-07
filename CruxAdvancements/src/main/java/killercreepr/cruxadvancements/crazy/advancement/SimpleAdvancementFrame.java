package killercreepr.cruxadvancements.crazy.advancement;

import eu.endercentral.crazy_advancements.advancement.AdvancementDisplay;
import killercreepr.crux.core.data.SimpleKeyed;
import killercreepr.cruxadvancements.api.crazy.advancement.AdvancementFrame;
import net.kyori.adventure.key.Key;

public class SimpleAdvancementFrame extends SimpleKeyed implements AdvancementFrame {
    public SimpleAdvancementFrame(Key key) {
        super(key);
    }

    public static AdvancementFrame fromCrazyAdvancement(AdvancementDisplay.AdvancementFrame frame){
        return switch (frame){
            case GOAL -> AdvancementFrame.GOAL;
            case TASK -> AdvancementFrame.TASK;
            case CHALLENGE -> AdvancementFrame.CHALLENGE;
        };
    }
}
