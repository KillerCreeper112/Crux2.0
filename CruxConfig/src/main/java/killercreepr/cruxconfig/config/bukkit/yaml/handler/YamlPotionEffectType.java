package killercreepr.cruxconfig.config.bukkit.yaml.handler;

import killercreepr.crux.util.CruxKey;
import net.kyori.adventure.key.Key;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class YamlPotionEffectType extends YamlGenericKeyed<PotionEffectType> {
    @Override
    public @Nullable PotionEffectType deserializeFromKey(@NotNull Key key) {
        return Registry.POTION_EFFECT_TYPE.get(CruxKey.key(key));
        //return PotionEffectType.getByKey(key); // Used for backwards compatibility.
    }
}
