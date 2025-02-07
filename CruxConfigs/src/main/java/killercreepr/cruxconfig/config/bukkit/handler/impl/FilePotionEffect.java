package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.core.util.CruxKey;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import net.kyori.adventure.key.Key;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@JsonSerializer(id = "potion_effect")
public class FilePotionEffect extends SimpleFileHandler<PotionEffect> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull PotionEffect object) {
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
    public @Nullable PotionEffect deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        PotionEffectType type = Registry.MOB_EFFECT.get(CruxKey.key(Key.key(o.getOrDefaultObject("type", "a"))));
        //for backwards compatibility PotionEffectType type = PotionEffectType.getByKey(NamespacedKey.fromString(o.getOrDefaultObject("type", "a")));
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

    @Override
    public @NotNull String jsonSerializerID() {
        return "potion_effect";
    }
}
