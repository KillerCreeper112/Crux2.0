package killercreepr.cruxenchants.core.registry;

import killercreepr.crux.core.registry.SimpleKeyedRegistry;
import killercreepr.cruxenchants.api.enchant.ApplicableItemType;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ApplicableItemTypeRegistry extends SimpleKeyedRegistry<ApplicableItemType> {
    public ApplicableItemTypeRegistry(@NotNull Map<Key, ApplicableItemType> map) {
        super(map);
    }

    public ApplicableItemTypeRegistry() {
    }
    
    protected final Map<Key, ApplicableItemType> itemTypeToApplicable = new HashMap<>();
    
    
}
