package killercreepr.cruxconfig.config.bukkit.yaml.handler;

import org.bukkit.NamespacedKey;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class YamlPotionEffectType extends YamlGenericKeyed<PotionEffectType> {
    @Override
    public @Nullable PotionEffectType deserializeFromKey(@NotNull NamespacedKey key) {
        return PotionEffectType.getByKey(key); // Used for backwards compatibility.
    }
}
