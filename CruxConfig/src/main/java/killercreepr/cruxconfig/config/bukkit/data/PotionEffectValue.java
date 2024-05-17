package killercreepr.cruxconfig.config.bukkit.data;

import killercreepr.crux.Crux;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.value.ConfigValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PotionEffectValue extends ConfigValue<PotionEffect> {
    public PotionEffectValue(@Nullable PotionEffect defaultValue) {
        super(PotionEffect.class, defaultValue);
    }

    public PotionEffectValue() {
        this(null);
    }

    @Override
    public @Nullable PotionEffect get(@NotNull CruxConfig cfg, @NotNull String path) {
        String s = cfg.config().getString(addDot(path) + "type");
        if(s == null) return null;
        PotionEffectType type = PotionEffectType.getByKey(Crux.minecraftKey(s));
        if(type == null) return null;
        int dur = cfg.config().getInt(addDot(path) + "duration");
        return new PotionEffect(type, dur, cfg.config().getInt(addDot(path) + "amplifier"),
                cfg.config().getBoolean(addDot(path) + "ambient", false),
                cfg.config().getBoolean(addDot(path) + "particles", true),
                cfg.config().getBoolean(addDot(path) + "icon", true));
    }

    @Override
    public void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable PotionEffect object) {
        cfg.config().set(removeDot(path),null);
        if(object==null) return;
        cfg.config().set(addDot(path) + "type", object.getType().getKey().asString());
        cfg.config().set(addDot(path) + "duration", object.getDuration());
        cfg.config().set(addDot(path) + "amplifier", object.getAmplifier());
        cfg.config().set(addDot(path) + "ambient", object.isAmbient());
        cfg.config().set(addDot(path) + "particles", object.hasParticles());
        cfg.config().set(addDot(path) + "icon", object.hasIcon());
    }
}
