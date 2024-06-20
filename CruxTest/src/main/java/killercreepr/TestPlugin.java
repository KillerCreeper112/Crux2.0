package killercreepr;

import killercreepr.crux.Crux;
import killercreepr.crux.CruxCore;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.registries.CruxModuleRegistry;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxconfig.config.bukkit.handler.BukkitCfgHandlers;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.cruxpotions.registries.CruxPotionRegistries;
import killercreepr.potion.TestPotion;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class TestPlugin extends CruxPlugin implements Listener {
    protected final CruxModuleRegistry MODULES = CruxRegistries.MODULES;
    protected final CruxCore CRUX_CORE = new CruxCore();
    //protected final CruxMenu CRUX_MENU = new CruxMenu();
    //protected final CruxPotionsModule CRUX_POTION = new CruxPotionsModule();

    @Override
    public void enabled() {
        Crux.setMainPlugin(this);
        super.enabled();

        //register modules.
        //they will automatically add in their listeners
        MODULES.register(this,
                CRUX_CORE
                //CRUX_MENU,
                //CRUX_POTION
        );

        BukkitCfgHandlers.init(CfgRegistries.JSON);
        BukkitCfgHandlers.init(CfgRegistries.YAML);
        BukkitCfgHandlers.initJson(CfgRegistries.JSON);
        BukkitCfgHandlers.initYaml(CfgRegistries.YAML);

        reloadConfigs();
        registerListeners(this);

        //CRUX_MENU.reload(this);

        new CClaimTags(Crux.TAGS);

        CruxPotionRegistries.POTIONS.register(new TestPotion());
    }

    record TestBoi(String name, int x){
    }

    @Override
    public void disabled() {
        super.disabled();
        MODULES.unregisterAll(this);
    }

    @Override
    public void reloadConfigs() {
        super.reloadConfigs();
        //CONFIGS.reload();
        MODULES.reload(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        reloadConfigs();
        return super.onCommand(sender, command, label, args);
    }
}
