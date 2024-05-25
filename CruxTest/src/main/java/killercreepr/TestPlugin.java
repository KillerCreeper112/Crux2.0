package killercreepr;

import io.papermc.paper.event.player.ChatEvent;
import killercreepr.crux.Crux;
import killercreepr.crux.menu.bukkit.ConfigMenu;
import killercreepr.crux.menu.bukkit.holder.MenuHolder;
import killercreepr.crux.menu.bukkit.registry.MenuRegistry;
import killercreepr.crux.tags.format.Format;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
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
    protected final MenuRegistry menuRegistry = new MenuRegistry(Crux.FORMAT);

    @Override
    public void onEnable() {
        Crux.setInstance(this); //todo stop this doodoo
        super.onEnable();
        BukkitCfgHandlers.initJson(CfgRegistries.JSON);
        BukkitCfgHandlers.initYaml(CfgRegistries.YAML);

        cfg = new Config(this, "config");
        cfg.setup();
        getServer().getPluginManager().registerEvents(this, this);

        menuRegistry.register(CfgRegistries.YAML);
        menuRegistry.loadConfiguration(new CruxFolder(this, "menus").file());
    }
    protected Config cfg;

    @EventHandler(ignoreCancelled = true)
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        menuRegistry.MENU_HOLDERS.get(Crux.key("testayo")).open(event.getPlayer());
        /*if(event.isSneaking()){
            cfg.MSG_1.use(event.getPlayer());
        }else cfg.MSG_2.use(event.getPlayer());*/
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player p = event.getPlayer();
        cfg.SWAP_HAND_EFFECTS.value().forEach(ee -> p.addPotionEffect(ee));
    }

    @EventHandler(ignoreCancelled = true)
    public void onChat(ChatEvent event) {
        getLogger().log(Level.WARNING, "Rleoading configs");
        cfg.setup();
    }
}
