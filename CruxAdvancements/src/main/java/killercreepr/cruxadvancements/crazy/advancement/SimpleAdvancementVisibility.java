package killercreepr.cruxadvancements.crazy.advancement;

import killercreepr.cruxadvancements.api.crazy.advancement.AdvancementVisibility;

public class SimpleAdvancementVisibility implements AdvancementVisibility {
    protected final String name;

    public SimpleAdvancementVisibility(String name) {
        this.name = name;
    }

    public eu.endercentral.crazy_advancements.advancement.AdvancementVisibility toCrazy(){
        return eu.endercentral.crazy_advancements.advancement.AdvancementVisibility.parseVisibility(name);
    }

    public static AdvancementVisibility fromCrazy(eu.endercentral.crazy_advancements.advancement.AdvancementVisibility eu){
        if(eu.getName().equalsIgnoreCase("ALWAYS")) return AdvancementVisibility.ALWAYS;
        if(eu.getName().equalsIgnoreCase("PARENT_GRANTED")) return AdvancementVisibility.PARENT_GRANTED;
        if(eu.getName().equalsIgnoreCase("VANILLA")) return AdvancementVisibility.VANILLA;
        if(eu.getName().equalsIgnoreCase("HIDDEN")) return AdvancementVisibility.HIDDEN;
        return AdvancementVisibility.ALWAYS;
    }

    public String getName() {
        return name;
    }
}
