package killercreepr;

import killercreepr.crux.Crux;
import killercreepr.crux.CruxCore;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.registries.CruxModuleRegistry;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.crux.tags.ab.container.MultiTagContainer;
import killercreepr.crux.tags.ab.container.StringTagContainer;
import killercreepr.crux.tags.ab.context.FormatPrefix;
import killercreepr.crux.tags.ab.hook.ObjectTag;
import killercreepr.crux.tags.ab.resolver.TagResolver;
import killercreepr.crux.tags.ab.tags.Format;
import killercreepr.crux.tags.ab.tags.Tags;
import killercreepr.crux.tags.defaults.CClaimTags;
import killercreepr.cruxconfig.config.bukkit.handler.BukkitCfgHandlers;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.cruxpotions.registries.CruxPotionRegistries;
import killercreepr.potion.TestPotion;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

        Tags tags = new Tags();
        Format fm = new Format(MiniMessage.miniMessage(), tags);

        MultiTagContainer ts = new MultiTagContainer(tags);
        ts.getStringTags().add(Tags.parsed("test", "ayo123"));
        ts.getStringTags().add(Tags.parsed("testagain", "onceagain122"), new FormatPrefix() {
            @Override
            public @NotNull String buildPrefix(@NotNull TagResolver<?> resolver) {
                return "ayo_";
            }
        });

        tags.getTags().add(new ObjectTag<TestBoi>() {
            @Override
            public @NotNull Class<TestBoi> getObjectType() {
                return TestBoi.class;
            }

            @Override
            public @NotNull FormatPrefix defaultPrefix() {
                return new FormatPrefix() {
                    @Override
                    public @NotNull String buildPrefix(@NotNull TagResolver<?> resolver) {
                        return "player_";
                    }
                };
            }

            @Override
            public @Nullable StringTagContainer requestStrings(@NotNull TestBoi p, @NotNull Tags tags) {
                return new StringTagContainer(tags)
                    .add(Tags.parsed("name", p.name + ""))
                    .add(Tags.parsed("x", p.x() + ""))
                    ;
            }
        });

        ts.hook(new TestBoi("killercreepr", 22));

        log(fm.deserializeString("This is a test for sure! <test> and <ayo_testagain> and lets see <player_name> or <player_x> <x>", ts));
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
