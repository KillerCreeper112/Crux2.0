package killercreepr.crux.persistence;

import killercreepr.crux.persistence.impl.CruxKeyTagType;
import killercreepr.crux.persistence.impl.PotionEffectListTagType;
import killercreepr.crux.persistence.impl.PotionEffectTagType;
import killercreepr.crux.persistence.impl.UUIDTagType;

public class CruxPersistence {
    public static final UUIDTagType UUID = new UUIDTagType();
    public static final PotionEffectTagType POTION_EFFECT = new PotionEffectTagType();
    public static final PotionEffectListTagType POTION_EFFECT_LIST = new PotionEffectListTagType();
    public static final CruxKeyTagType CRUX_KEY = new CruxKeyTagType();

}
