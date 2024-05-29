package killercreepr.crux.plugin;

import killercreepr.crux.module.CruxModule;
import killercreepr.crux.registries.Registries;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

//todo use better command framework
public abstract class CruxPlugin extends JavaPlugin implements CruxModule {
    /**
     * Use enabled() for onEnable logic.
     */
    @Override
    public final void onEnable() {
        super.onEnable();

        Registries.PLUGINS.register(this);

        enabled();
    }

    public void enabled(){
        reloadConfigs();
        //registerDefaultCmds();
    }

    @Override
    public @NotNull String name() {
        return this.getName();
    }

    /**
     * Use disabled() for onDisable logic.
     */
    @Override
    public final void onDisable() {
        super.onDisable();
        HandlerList.unregisterAll((Plugin) this);
        Registries.PLUGINS.unregister(this);
        disabled();
    }

    public void disabled(){}

    /*public final Map<String, CruxCmd> COMMANDS = new HashMap<>();
    public void registerDefaultCmds(){
    }

    public void registerCmd(@NotNull CruxCmd... cmd){
        for(CruxCmd c : cmd){
            COMMANDS.put(c.id(), c);
        }
    }

    public @NotNull String getCmdID(@NotNull Command cmd, @NotNull String[] args){
        return CruxCmd.id(cmd.getName().toLowerCase(), args.length > 0 ? args[0].toLowerCase() : null);
    }*/

    /*@Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        command(sender, cmd, args);
        return true;
    }*/

    /*@Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if(args.length == 1){
            for(CruxCmd c : COMMANDS.values()){
                if(!c.cmd().equalsIgnoreCase(cmd.getName())) continue;
                if(c.subAction() == null) continue;
                list.add(c.subAction());
            }
        }
        CruxCmd command = getCommand(cmd, args);
        List<String> cmdTab = command == null ? null : command.tabComplete(sender, args);
        if(cmdTab != null) list.addAll(cmdTab);
        return list;
    }

    protected void command(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String[] args){
        CruxCmd command = getCommand(cmd, args);
        if(command != null) command.command(sender, args);
    }

    protected @Nullable CruxCmd getCommand(@NotNull Command cmd, @NotNull String[] args){
        CruxCmd c = COMMANDS.get(getCmdID(cmd, args));
        if(c!=null) return c;
        return COMMANDS.get(cmd.getName());
    }*/

    public void registerListeners(@NotNull Listener... listeners){
        for(Listener l : listeners){
            getServer().getPluginManager().registerEvents(l, this);
        }
    }
    public void reloadConfigs(){}

    /*protected CruxConfig registerCfg(@NotNull CruxConfig c){
        c.createDefault();
        c.register();
        return c;
    }

    protected void registerCfgs(@NotNull CruxConfig... c){
        for(CruxConfig cfg : c){
            registerCfg(cfg);
        }
    }*/

    /*public @NotNull NamespacedKey key(@NotNull String key){
        return key(key.split(":"));
    }

    public @NotNull NamespacedKey key(@NotNull String[] key){
        return key.length > 1 ? new NamespacedKey(key[0], key[1]) : new NamespacedKey(this, key[0]);
    }

    public @NotNull String getKeyAsMinimalString(@NotNull NamespacedKey k){
        return hasPluginNamespace(k) ? k.getKey() : k.asString();
    }

    public boolean hasPluginNamespace(@NotNull NamespacedKey k){
        return k.getNamespace().equalsIgnoreCase(key("a").getNamespace());
    }*/

    public CruxPlugin log(@NotNull Level level, @NotNull String text){
        getLogger().log(level, text);
        return this;
    }

    public CruxPlugin log(@NotNull String info){
        return log(Level.INFO, info);
    }

    public CruxPlugin logWarning(@NotNull String info){
        return log(Level.WARNING, info);
    }

    public CruxPlugin logError(@NotNull String info){
        return log(Level.SEVERE, info);
    }
}
