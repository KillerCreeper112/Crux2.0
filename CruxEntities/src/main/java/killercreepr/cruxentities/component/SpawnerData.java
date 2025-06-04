package killercreepr.cruxentities.component;

import net.kyori.adventure.key.Key;

import java.util.Map;

public class SpawnerData {
    protected final Map<Key, Key> fromVanillaToCustom;

    public SpawnerData(Map<Key, Key> fromVanillaToCustom) {
        this.fromVanillaToCustom = fromVanillaToCustom;
    }

    public Map<Key, Key> getFromVanillaToCustom() {
        return fromVanillaToCustom;
    }
}
