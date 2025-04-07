package killercreepr.cruxadvancements.api.crazy.advancement;

import killercreepr.crux.api.data.CruxKeyed;
import killercreepr.cruxadvancements.crazy.advancement.SimpleAdvancementFrame;
import net.kyori.adventure.key.Key;

public interface AdvancementFrame extends CruxKeyed {
    AdvancementFrame TASK = new SimpleAdvancementFrame(Key.key("task"));
    AdvancementFrame GOAL = new SimpleAdvancementFrame(Key.key("goal"));
    AdvancementFrame CHALLENGE = new SimpleAdvancementFrame(Key.key("challenge"));
}
