package killercreepr.cruxadvancements.api.crazy.advancement;

import killercreepr.cruxadvancements.crazy.advancement.SimpleAdvancementVisibility;

public interface AdvancementVisibility {
    AdvancementVisibility ALWAYS = new SimpleAdvancementVisibility("ALWAYS");
    AdvancementVisibility PARENT_GRANTED = new SimpleAdvancementVisibility("PARENT_GRANTED");
    AdvancementVisibility VANILLA = new SimpleAdvancementVisibility("VANILLA");
    AdvancementVisibility HIDDEN = new SimpleAdvancementVisibility("HIDDEN");
}
