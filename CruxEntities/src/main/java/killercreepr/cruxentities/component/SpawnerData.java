package killercreepr.cruxentities.component;

import net.kyori.adventure.key.Key;

import java.util.Map;

public class SpawnerData {
    protected final Map<Key, MobData> fromVanillaToCustom;

    public SpawnerData(Map<Key, MobData> fromVanillaToCustom) {
        this.fromVanillaToCustom = fromVanillaToCustom;
    }

    public Map<Key, MobData> getFromVanillaToCustom() {
        return fromVanillaToCustom;
    }

    public static class MobData{
        protected final Key mobType;
        protected final boolean applyEquipment;

        public MobData(Key mobType, boolean applyEquipment) {
            this.mobType = mobType;
            this.applyEquipment = applyEquipment;
        }

        public Key getMobType() {
            return mobType;
        }

        public boolean isApplyEquipment() {
            return applyEquipment;
        }
    }
}
