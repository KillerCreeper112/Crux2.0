package killercreepr.cruxpotions.persistence;

import killercreepr.crux.core.registries.CruxRegistries;

public class CruxPotionsPersistence {
    public static void register(){}
    public static final CustomPotionEffectTagType CUSTOM_POTION_EFFECT = CruxRegistries.PERSISTENT_DATA_TYPE.register(new CustomPotionEffectTagType());
    public static final CustomPotionEffectListTagType CUSTOM_POTION_EFFECT_LIST = CruxRegistries.PERSISTENT_DATA_TYPE.register(new CustomPotionEffectListTagType());
}
