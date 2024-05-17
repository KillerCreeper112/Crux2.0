package killercreepr.cruxconfig.config.bukkit.data;

import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.Nullable;

public class PotionEffectValue extends CommonValue<PotionEffect> {
    public PotionEffectValue(@Nullable PotionEffect defaultValue) {
        super(PotionEffect.class, defaultValue);
    }

    public PotionEffectValue() {
        this(null);
    }
}
