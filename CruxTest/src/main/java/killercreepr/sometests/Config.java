package killercreepr.sometests;

import killercreepr.crux.data.CreateSound;
import killercreepr.crux.data.CreateTitle;
import killercreepr.crux.data.MsgContainer;
import killercreepr.cruxconfig.config.bukkit.file.Cfg;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.value.CfgValue;
import killercreepr.cruxconfig.config.bukkit.value.CommonValue;
import killercreepr.cruxconfig.config.bukkit.value.MsgValue;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.time.Duration;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Config extends Cfg {
    public final CfgValue<Collection<PotionEffect>> SWAP_HAND_EFFECTS = new CommonValue<>(List.of(
            new PotionEffect(PotionEffectType.HEALTH_BOOST, 20, 20)
    )){};
    public final MsgValue RELOADING_CONFIGS = new MsgValue(new MsgContainer.Builder()
            .chat(List.of(
                    "<gray>Test msg!",
                    "<yellow>Ayo my man!"
            ))
            .sound(new CreateSound(Sound.BLOCK_SNIFFER_EGG_PLOP))
            .title(new CreateTitle("<yellow>Reloading configs", "sub sub sub", Title.Times.times(Duration.ZERO,
                    Duration.ofMillis(50L*40), Duration.ZERO)))
            .actionBar("<yellow>Now we talking")
            .build());
    public final CfgValue<Map<String, String>> STRINGS_MAN = new CommonValue<>(new LinkedHashMap<>(){{
        put("test", "another");
        put("dog", "cat");
    }}){};
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
