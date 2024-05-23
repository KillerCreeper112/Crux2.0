package killercreepr.sometests;

import killercreepr.cruxconfig.config.bukkit.file.Cfg;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.value.CfgValue;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

public class Config extends Cfg {
    public final CfgValue<Collection<Collection<PotionEffect>>> SWAP_HAND_EFFECTS = new CfgValue<>(List.of(
            List.of(
                    new PotionEffect(PotionEffectType.SPEED, 20, 20),
                    new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 100, 0, false, false, false)
            ),
            List.of(
                    new PotionEffect(PotionEffectType.STRENGTH, 20, 20),
                    new PotionEffect(PotionEffectType.HEALTH_BOOST, 100, 0, false, false, false)
            )
    )) {
        @Override
        public @Nullable Collection<Collection<PotionEffect>> get(@NotNull CruxConfig cfg, @NotNull String path) {
            Bukkit.getLogger().severe("AYO TEST: " + getParameterType());
            return (Collection<Collection<PotionEffect>>) cfg.deserializeObject(getParameterType(), path);
        }

        @Override
        public void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable Object object) {
            cfg.set(path, object);
        }

        @Override
        public void register(@NotNull CruxConfig cfg, @NotNull String path) {
            set(cfg, path, value);
            setValue(get(cfg, path));
        }
    };

    public final CfgValue<Collection<Collection<Integer>>> TEST = new CfgValue<>(List.of(
            List.of(
                    1, 2, 3, 4, 5, 6, 7, 8, 9
            ),
            List.of(
                    100, 105, 115, 200
            )
    )) {
        @Override
        public @Nullable Collection<Collection<Integer>> get(@NotNull CruxConfig cfg, @NotNull String path) {
            return (Collection<Collection<Integer>>) cfg.deserializeObject(getParameterType(), path);
        }

        @Override
        public void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable Object object) {
            cfg.set(path, object);
        }

        @Override
        public void register(@NotNull CruxConfig cfg, @NotNull String path) {
            set(cfg, path, value);
            setValue(get(cfg, path));
        }
    };
    /*public final MsgValue MSG_1 = new MsgValue("<red>This is not good");
    public final MsgValue MSG_2 = new MsgValue(new MsgContainer.Builder()
            .chat(List.of(
                    "<gray>Test msg!",
                    "<yellow>Ayo my man!"
            ))
            .sound(new CreateSound(Sound.BLOCK_SNIFFER_EGG_PLOP))
            .title(new CreateTitle("TITLE", "sub sub sub", Title.Times.times(Duration.ZERO,
                    Duration.ofMillis(50L*40), Duration.ZERO)))
            .actionBar("<yellow>Now we talking")
            .build());
    public final CfgValue<Map<Integer, Integer>> TEST_MAP = new CfgValue<>(new MapValue(
            new HashMap<>(){{
                put(1, 3);
                put(23, 10);
            }}, Integer.class
    ));*/
    public Config(@NotNull Plugin plugin, @NotNull String path) {
        super(plugin, path);
    }

    public Config(@NotNull File file) {
        super(file);
    }

    public Config(@NotNull CruxConfig cfg) {
        super(cfg);
    }

    @Override
    public void setup() {
        super.setup();
    }
}
