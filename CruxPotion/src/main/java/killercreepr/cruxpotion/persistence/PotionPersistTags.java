package killercreepr.cruxpotion.persistence;

import killercreepr.crux.persistence.PersistTag;

import java.util.Collection;

public class PotionPersistTags {
    public static void register(){}

    public static final PersistTag<Collection<CustomPotionHolder>> STORED_POTIONS =
            PersistTag.register(new PersistTag<>(CruxPotionPersistence.CUSTOM_POTION_EFFECT_LIST, "stored_potions"));
}
