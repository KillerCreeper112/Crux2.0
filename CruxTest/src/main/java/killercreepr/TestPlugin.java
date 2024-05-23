package killercreepr;

import io.papermc.paper.event.player.ChatEvent;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.handlers.BukkitCfgHandlers;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.sometests.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class TestPlugin extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        super.onEnable();
        BukkitCfgHandlers.initJson(CfgRegistries.JSON);
        BukkitCfgHandlers.initYaml(CfgRegistries.YAML);
        //cfg = new Config(this, "config");

        TestConfigValue<Collection<PotionEffect>> test = new TestConfigValue<>() {
            @Override
            public @Nullable Collection<PotionEffect> get(@NotNull CruxConfig cfg, @NotNull String path) {
                return List.of();
            }

            @Override
            public void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable Collection<PotionEffect> object) {

            }
        };
        test.save();

        TestConfigValue<Map<Integer, PotionEffect>> again = new TestConfigValue<>() {
            @Override
            public @Nullable Map<Integer, PotionEffect> get(@NotNull CruxConfig cfg, @NotNull String path) {
                return Map.of();
            }

            @Override
            public void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable Map<Integer, PotionEffect> object) {

            }
        };
        again.save();
        cfg = new Config(this, "config");
        cfg.setup();
        getServer().getPluginManager().registerEvents(this, this);
    }
    protected Config cfg;

    @EventHandler(ignoreCancelled = true)
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        /*if(event.isSneaking()){
            cfg.MSG_1.use(event.getPlayer());
        }else cfg.MSG_2.use(event.getPlayer());*/
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player p = event.getPlayer();
        getLogger().warning("LET's SEE?? " + cfg.SWAP_HAND_EFFECTS.value());
        Bukkit.getLogger().severe("HAHA: " + cfg.TEST.value());
        cfg.SWAP_HAND_EFFECTS.value().forEach(value ->{
            value.forEach(d -> p.addPotionEffect(d));
        });
        //p.sendMessage(cfg.TEST_MAP.value() + "");
        /*cfg.TEST_MAP.value().forEach((key, value) ->{
            p.sendMessage("key: " + key + " -> " + (value + 20));
        });*/
    }

    @EventHandler(ignoreCancelled = true)
    public void onChat(ChatEvent event) {
        getLogger().log(Level.WARNING, "Rleoading configs");
        cfg.setup();
    }
}
