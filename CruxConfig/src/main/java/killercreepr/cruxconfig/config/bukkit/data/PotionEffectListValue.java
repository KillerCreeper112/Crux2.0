package killercreepr.cruxconfig.config.bukkit.data;

import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.value.ConfigValue;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;

public class PotionEffectListValue extends ConfigValue<Collection<PotionEffect>> {

    public PotionEffectListValue(@Nullable Collection<PotionEffect> defaultValue) {
        super((Class<Collection<PotionEffect>>) (Class<?>) Collection.class, defaultValue);
    }

    public PotionEffectListValue() {
        this(null);
    }


    @Override
    public @Nullable Collection<PotionEffect> get(@NotNull CruxConfig cfg, @NotNull String path) {
        ConfigurationSection section = cfg.config().getConfigurationSection(removeDot(path));
        Collection<PotionEffect> list = new HashSet<>();
        if(section == null) return list;
        for(String s : section.getKeys(false)){
            PotionEffect e = new PotionEffectValue().get(cfg, addDot(path)+s);
            if(e != null) list.add(e);
        }
        return list;
    }

    @Override
    public void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable Collection<PotionEffect> object) {
        cfg.config().set(removeDot(path),null);
        if(object==null) return;
        int index = -1;
        for(PotionEffect p : object){
            index++;
            new PotionEffectValue().set(cfg, addDot(path) + index, p);
        }
    }
}
