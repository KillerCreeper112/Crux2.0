package killercreepr.crux.config.bukkit.data;

import killercreepr.crux.config.bukkit.CruxConfig;
import killercreepr.crux.config.bukkit.value.ConfigValue;
import org.bukkit.NamespacedKey;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PotionEffectTypeListValue extends ConfigValue<List<PotionEffectType>> {

    public PotionEffectTypeListValue(@Nullable List<PotionEffectType> defaultValue) {
        super((Class<List<PotionEffectType>>) (Class<?>) List.class, defaultValue);
    }

    public PotionEffectTypeListValue() {
        this(null);
    }

    @Nullable
    @Override
    public List<PotionEffectType> get(@NotNull CruxConfig cfg, @NotNull String path) {
        List<PotionEffectType> list = new ArrayList<>();
        for(String s : cfg.config().getStringList(removeDot(path))){
            String[] args = s.split(":");
            PotionEffectType type = PotionEffectType.getByKey(args.length > 1 ? new NamespacedKey(args[0], args[1]) :
                    NamespacedKey.minecraft(args[0]));
            if(type == null) continue;
            list.add(type);
        }
        return list.isEmpty() ? null : list;
    }

    @Override
    public void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable List<PotionEffectType> object) {
        cfg.config().set(removeDot(path),null);
        if(object==null) return;
        List<String> list = new ArrayList<>();
        object.forEach(p -> list.add(p.getKey().asString()));
        cfg.config().set(removeDot(path), list);
    }
}
