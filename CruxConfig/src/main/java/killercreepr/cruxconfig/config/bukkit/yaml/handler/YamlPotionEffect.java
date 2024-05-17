package killercreepr.cruxconfig.config.bukkit.yaml.handler;

import killercreepr.crux.Crux;
import killercreepr.cruxconfig.config.common.yaml.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class YamlPotionEffect implements YamlObjectHandler<PotionEffect> {
    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull PotionEffect object) {
        return new YamlObject()
                .addProperty("type", object.getType().getKey().asString())
                .addProperty("duration", object.getDuration())
                .addProperty("amplifier", object.getAmplifier())
                .addProperty("ambient", object.isAmbient())
                .addProperty("particles", object.hasParticles())
                .addProperty("icon", object.hasIcon())
                ;
    }

    @Override
    public @Nullable PotionEffect deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
        if(!(e instanceof YamlObject o)) return null;
        PotionEffectType type = PotionEffectType.getByKey(Crux.minecraftKey(o.getOrDefaultObject("type", "a")));
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
