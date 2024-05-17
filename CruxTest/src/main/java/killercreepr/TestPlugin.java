package killercreepr;

import io.papermc.paper.event.player.ChatEvent;
import killercreepr.cruxconfig.config.bukkit.json.handlers.BukkitJsonHandlers;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.sometests.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class TestPlugin extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        super.onEnable();
        BukkitJsonHandlers.initJson(CfgRegistries.JSON);
        BukkitJsonHandlers.initYaml(CfgRegistries.YAML);
        cfg = new Config(this, "config");
        cfg.setup();

        getServer().getPluginManager().registerEvents(this, this);
    }
    protected Config cfg;

    @EventHandler(ignoreCancelled = true)
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player p = event.getPlayer();
        cfg.SWAP_HAND_EFFECTS.value().forEach(p::addPotionEffect);
    }

    @EventHandler(ignoreCancelled = true)
    public void onChat(ChatEvent event) {
        getLogger().log(Level.WARNING, "Rleoading configs");
        cfg.setup();
    }
}
