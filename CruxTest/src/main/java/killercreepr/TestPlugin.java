package killercreepr;

import io.papermc.paper.event.player.ChatEvent;
import killercreepr.crux.Crux;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.Holder;
import killercreepr.crux.menu.bukkit.listener.MenuListener;
import killercreepr.crux.menu.bukkit.registry.MenuRegistry;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.tags.defaults.CClaimTags;
import killercreepr.crux.valueproviders.InputContext;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxconfig.config.bukkit.handlers.BukkitCfgHandlers;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.sometests.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class TestPlugin extends CruxPlugin implements Listener {
    protected final MenuRegistry menuRegistry = new MenuRegistry(Crux.FORMAT);

    @Override
    public void enabled() {
        super.enabled();
        BukkitCfgHandlers.initJson(CfgRegistries.JSON);
        BukkitCfgHandlers.initYaml(CfgRegistries.YAML);

        cfg = new Config(this, "config");
        cfg.setup();
        registerListeners(new MenuListener(this));
        getServer().getPluginManager().registerEvents(this, this);

        menuRegistry.register(CfgRegistries.YAML);
        menuRegistry.loadConfiguration(new CruxFolder(this, "menus").file());

        new CClaimTags(Crux.TAGS);
    }
    protected Config cfg;

    @EventHandler(ignoreCancelled = true)
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        menuRegistry.MENU_HOLDERS.get(Crux.key("testayo")).open(event.getPlayer(),
                DataExchange.builder()
                        .put("test_ayo", Holder.direct(new CClaimTags.TestBois("test_bois")))
                        .build()).getMenu().setItem(13, cfg.DYANMIC.value().build(
                new InputContext() {
                    @Override
                    public @NotNull String input(@NotNull String text) {
                        return text;
                    }
                }
        ).item());
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
        menuRegistry.loadConfiguration(new CruxFolder(this, "menus").file());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        cfg.setup();
        menuRegistry.loadConfiguration(new CruxFolder(this, "menus").file());
        return super.onCommand(sender, command, label, args);
    }
}
