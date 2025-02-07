package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.core.util.CruxKey;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import net.kyori.adventure.key.Key;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonSerializer(id = "potion_effect_type")
public class FilePotionEffectType extends FileGenericKeyed<PotionEffectType> {
    @Override
    public @Nullable PotionEffectType deserializeFromKey(@NotNull Key key) {
        return Registry.MOB_EFFECT.get(CruxKey.key(key));
        //return PotionEffectType.getByKey(key); // Used for backwards compatibility.
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "potion_effect_type";
    }
}
