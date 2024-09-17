package killercreepr.crux.plugin;

import killercreepr.crux.module.CruxModule;
import killercreepr.crux.registries.CruxRegistries;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public abstract class CruxPlugin extends JavaPlugin implements CruxModule {
    /**
     * Use enabled() for onEnable logic.
     */
    @Override
    public final void onEnable() {
        super.onEnable();

        CruxRegistries.PLUGIN.register(this);

        enabled();
    }

    public void enabled(){
        reload();
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
        CruxRegistries.PLUGIN.unregister(this);
        disabled();
    }

    public void disabled(){}

    public void registerListeners(@NotNull Listener... listeners){
        for(Listener l : listeners){
            getServer().getPluginManager().registerEvents(l, this);
        }
    }
    public void reload(){}

    @Override
    public void reload(@NotNull CruxPlugin plugin) {
        CruxModule.super.reload(plugin);
        reload();
    }

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
