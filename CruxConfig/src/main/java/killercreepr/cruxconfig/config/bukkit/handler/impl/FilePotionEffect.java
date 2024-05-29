package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.cruxconfig.config.bukkit.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.bukkit.NamespacedKey;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FilePotionEffect extends SimpleFileHandler<PotionEffect> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull PotionEffect object) {
        return new FileObject()
                .addProperty("type", object.getType().getKey().asString())
                .addProperty("duration", object.getDuration())
                .addProperty("amplifier", object.getAmplifier())
                .addProperty("ambient", object.isAmbient())
                .addProperty("particles", object.hasParticles())
                .addProperty("icon", object.hasIcon())
                ;
    }

    @Override
    public @Nullable PotionEffect deserializeFromFile(@NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        PotionEffectType type = PotionEffectType.getByKey(NamespacedKey.fromString(o.getOrDefaultObject("type", "a")));
        if(type==null) return null;
        return new PotionEffect(
                type,
                o.getOrDefaultObject("duration", 0),
                o.getOrDefaultObject("amplifier", 0),
                o.getOrDefaultObject("ambient", false),
                o.getOrDefaultObject("particles", true),
                o.getOrDefaultObject("icon", true)
        );
    }
}
