package killercreepr.cruxpotions.persistence;

import killercreepr.crux.core.persistence.PersistTag;

import java.util.Collection;

public class PotionPersistTags {
    public static void register(){}

    public static final PersistTag<Collection<StoredPotion>> STORED_CUSTOM_POTIONS =
            PersistTag.register(new PersistTag<>(CruxPotionsPersistence.CUSTOM_POTION_EFFECT_LIST, "stored_custom_potions"));
}
