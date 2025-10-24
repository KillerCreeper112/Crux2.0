package killercreepr.crux.core.plugin.module;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.api.event.ServerShutDownEvent;
import killercreepr.crux.api.plugin.module.CruxModule;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.command.CruxLootCommands;
import killercreepr.crux.core.component.CruxComponents;
import killercreepr.crux.core.entity.consumer.OminousStandardEntityConsumer;
import killercreepr.crux.core.external.placeholderapi.FormatMillisecondsHook;
import killercreepr.crux.core.external.placeholderapi.FormatTicksHook;
import killercreepr.crux.core.external.placeholderapi.TagsExpansionHook;
import killercreepr.crux.core.listener.EntityDataListener;
import killercreepr.crux.core.listener.EntityMechanicsListener;
import killercreepr.crux.core.listener.EntitySpawnListener;
import killercreepr.crux.core.listener.LootContainerListener;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.registries.CruxRegistries;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CruxMainModule implements CruxModule, Listener {
    public static final String NAMESPACE = StandardModules.CRUX_MAIN;
    @Override
    public @NotNull String name() {
        return NAMESPACE;
    }

    static {
        //ComponentParserTypes.register();
    }

    protected final Collection<String> stopCommands = Set.of(
        "stop", "bukkit:stop", "minecraft:stop", "spigot:stop"
    );
    protected final Collection<String> restartCommands = Set.of(
         "spigot:restart", "restart", "bukkit:restart", "minecraft:restart"
    );
    //bukkit.command.reload
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onServerCommand(ServerCommandEvent event) {
        String cmd = event.getCommand().toLowerCase();
        if(cmd.startsWith("/")) cmd = cmd.substring(1);
        handleStopRestartLogic(cmd, event.getSender());
    }

    public void handleStopRestartLogic(String cmd, CommandSender sender){
        if(stopCommands.contains(cmd)){
            if(!sender.hasPermission("minecraft.command.stop")) return;
            onStop();
            return;
        }
        if(restartCommands.contains(cmd)){
            if(!sender.hasPermission("bukkit.command.restart")) return;
            onStop();
        }
    }

    public void onStop(){
        Crux.log(Level.INFO, "Server is shutting down!");

        for(EntityMemory data : EntityMemory.REGISTRY){
            data.forceRemoveDataHolders(data.value());
        }

        ServerShutDownEvent event = new ServerShutDownEvent();
        event.callEvent();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String cmd = event.getMessage().toLowerCase();
        if(cmd.startsWith("/")) cmd = cmd.substring(1);
        handleStopRestartLogic(cmd, event.getPlayer());
    }

    @Override
    public void onLoad(@NotNull CruxPlugin plugin) {
        CruxComponents.register();
        CruxLootCommands.register(plugin);

        CruxRegistries.ENTITY_CONSUMER.register(new OminousStandardEntityConsumer(Crux.key("ominous/standard")));

        if(plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null){
            try{
                new TagsExpansionHook("crux", Crux.format()).register();
                new FormatTicksHook().register();
                new FormatMillisecondsHook().register();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Logger.getLogger("com.joestelmach.natty").setLevel(Level.WARNING);
    }

    @Override
    public void onEnable(@NotNull CruxPlugin plugin) {
        plugin.registerListeners(
            new EntityDataListener(),
            new EntityMechanicsListener(),
            new EntitySpawnListener(),
            new LootContainerListener(),
            this
        );
        Crux.buildTickTask().runTaskTimerAsynchronously(plugin, 20L, 1L);
        Crux.buildMainThreadTickTask().runTaskTimer(plugin, 20L, 1L);
    }
}
