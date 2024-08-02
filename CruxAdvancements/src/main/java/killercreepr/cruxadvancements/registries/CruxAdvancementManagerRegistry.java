package killercreepr.cruxadvancements.registries;

import killercreepr.crux.registry.SimpleKeyedRegistry;
import killercreepr.cruxadvancements.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CruxAdvancementManagerRegistry extends SimpleKeyedRegistry<CruxAdvancementManager<?>> {
    public CruxAdvancementManagerRegistry(@NotNull Map<Key, CruxAdvancementManager<?>> map) {
        super(map);
    }

    public CruxAdvancementManagerRegistry() {
        super();
    }

    public @Nullable CruxAdvancement getAdvancement(@NotNull Key managerKey, @NotNull Key advancementKey){
        return getAdvancement(managerKey, advancementKey, null);
    }

    public CruxAdvancement getAdvancement(@NotNull Key managerKey, @NotNull Key advancementKey, @Nullable CruxAdvancement defaultValue){
        CruxAdvancementManager<?> manager = get(managerKey);
        if(manager==null) return defaultValue;
        CruxAdvancement a = manager.getAdvancement(advancementKey);
        return a==null?defaultValue:a;
    }
}
