package killercreepr.cruxadvancements.core.data;

import killercreepr.crux.core.Crux;
import killercreepr.cruxadvancements.api.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.api.advancement.manager.CruxAdvancementManager;
import killercreepr.cruxadvancements.core.registries.AdvancementRegistries;
import net.kyori.adventure.key.Key;

import java.util.Objects;

public class AdvancementPair {
    public static AdvancementPair pair(Key manager, Key advancement){
        return new AdvancementPair(manager, advancement);
    }

    public static AdvancementPair pair(Key advancement){
        return pair(extractManagerKey(advancement), advancement);
    }

    public static Key extractManagerKey(Key advancement){
        return Crux.key(advancement.toString().split("/", 2)[0]);
    }

    protected final Key managerKey;
    protected final Key advancementKey;

    public AdvancementPair(Key managerKey, Key advancementKey) {
        this.managerKey = managerKey;
        this.advancementKey = advancementKey;
    }

    public Key getManagerKey() {
        return managerKey;
    }

    public Key getAdvancementKey() {
        return advancementKey;
    }

    public CruxAdvancement getAdvancement(){
        CruxAdvancementManager<?> manager = getManager();
        if(manager==null) return null;
        return manager.getAdvancement(advancementKey);
    }

    public CruxAdvancement getAdvancementOrThrow(){
        CruxAdvancementManager<?> manager = getManager();
        Objects.requireNonNull(manager, "CruxAdvancementManager not found! " + this);
        CruxAdvancement a = getAdvancement();
        Objects.requireNonNull(a, "CruxAdvancement not found! " + this);
        return a;
    }


    public CruxAdvancementManager<?> getManager(){
        return AdvancementRegistries.ADVANCEMENT_MANAGERS.get(managerKey);
    }
}
