package killercreepr;

import io.papermc.paper.event.player.ChatEvent;
import killercreepr.crux.Crux;
import killercreepr.crux.CruxCore;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.Holder;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.registries.CruxModuleRegistry;
import killercreepr.crux.registries.Registries;
import killercreepr.crux.tags.defaults.CClaimTags;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxconfig.config.bukkit.handler.BukkitCfgHandlers;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.cruxmenu.CruxMenu;
import killercreepr.cruxpotion.CruxPotion;
import killercreepr.sometests.Config;
import killercreepr.sometests.TestConfigs;
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
    protected final CruxModuleRegistry MODULES = Registries.MODULES;
    protected final CruxCore CRUX_CORE = new CruxCore();
    protected final CruxMenu CRUX_MENU = new CruxMenu();
    protected final CruxPotion CRUX_POTION = new CruxPotion();

    protected TestConfigs CONFIGS;
    @Override
    public void enabled() {
        super.enabled();

        //register modules.
        //they will automatically add in their listeners
        MODULES.register(this,
                CRUX_CORE,
                CRUX_MENU,
                CRUX_POTION
        );

        BukkitCfgHandlers.initJson(CfgRegistries.JSON);
        BukkitCfgHandlers.initYaml(CfgRegistries.YAML);

        CONFIGS = new TestConfigs(
                new Config(this, "config")
        );
        reloadConfigs();
        registerListeners(this);

        CRUX_MENU.reload(this);

        new CClaimTags(Crux.TAGS);
    }

    public TestConfigs cfgs() {
        return CONFIGS;
    }

    @Override
    public void disabled() {
        super.disabled();
        MODULES.unregisterAll(this);
    }

    @Override
    public void reloadConfigs() {
        super.reloadConfigs();
        CONFIGS.reload();
        MODULES.reload(this);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        CRUX_MENU.menuRegistry().MENU_HOLDERS.get(Crux.key("testayo")).open(event.getPlayer(),
                DataExchange.builder()
                        .put("test_ayo", Holder.direct(new CClaimTags.TestBois("test_bois")))
                        .build());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player p = event.getPlayer();
        cfgs().CFG.SWAP_HAND_EFFECTS.value().forEach(ee -> p.addPotionEffect(ee));
    }

    @EventHandler(ignoreCancelled = true)
    public void onChat(ChatEvent event) {
        getLogger().log(Level.WARNING, "Rleoading configs");
        cfgs().CFG.setup();
        CRUX_MENU.menuRegistry().loadConfiguration(new CruxFolder(this, "menus").file());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        reloadConfigs();
        return super.onCommand(sender, command, label, args);
    }
}
