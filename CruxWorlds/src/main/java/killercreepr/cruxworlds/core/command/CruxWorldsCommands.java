package killercreepr.cruxworlds.core.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.util.CruxWorldUtil;
import killercreepr.cruxworlds.api.world.CruxWorld;
import killercreepr.cruxworlds.api.world.manager.CruxWorldManager;
import killercreepr.cruxworlds.api.world.type.CruxWorldType;
import killercreepr.cruxworlds.core.command.arguments.CruxWorldArgs;
import killercreepr.cruxworlds.core.command.arguments.CruxWorldArgument;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class CruxWorldsCommands {
    protected final String cmdName;
    protected final String cmdPermission;
    protected final List<String> cmdAliases;
    protected final CruxWorldManager worldManager;

    protected final CruxWorldArgument worldArg;

    public CruxWorldsCommands(String cmdName, String cmdPermission, List<String> cmdAliases, CruxWorldManager worldManager) {
        this.cmdName = cmdName;
        this.cmdPermission = cmdPermission;
        this.cmdAliases = cmdAliases;
        this.worldManager = worldManager;
        this.worldArg = new CruxWorldArgument(worldManager);
    }

    public void register(@NotNull CruxPlugin plugin){
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event ->{
            final Commands commands = event.registrar();
            LiteralCommandNode<CommandSourceStack> cmd = build(Commands.literal(cmdName)
                .requires(source -> source.getSender().hasPermission(cmdPermission)),
                plugin.getLifecycleManager());
            commands.register(cmd, cmdAliases);
        });
    }

    public LiteralCommandNode<CommandSourceStack> build(LiteralArgumentBuilder<CommandSourceStack> dispatcher,
                                                                  LifecycleEventManager<?> manager){
        ///effect give killercreepr minecraft:absorption 1 2 true
        //cruxpotion give <player> <potion> <duration> <amplifier> <hide_particles>
        dispatcher.then(
            Commands.literal("unload")
                .then(
                    Commands.argument("world", worldArg)
                        .executes(ctx ->{
                            CommandSender sender = getExecutor(ctx.getSource());
                            CruxWorld world = ctx.getArgument("world", CruxWorld.class);
                            sender.sendMessage("Unloading world " + world.getName() + "...");
                            worldManager.unloadWorld(world, true).whenComplete((success, throwable) ->{
                                if(throwable != null) Crux.log(Level.SEVERE, throwable.getMessage());
                                if(!success){
                                    sender.sendMessage("Could not unload world, " + world.getName() + "...");
                                    return;
                                }
                                sender.sendMessage("Unloaded world, " + world.getName() + ".");
                            });
                            return 1;
                        })
                        .then(
                            Commands.argument("save", BoolArgumentType.bool())
                                .executes(ctx ->{
                                    CommandSender sender = getExecutor(ctx.getSource());
                                    CruxWorld world = ctx.getArgument("world", CruxWorld.class);
                                    sender.sendMessage("Unloading world " + world.getName() + "...");
                                    worldManager.unloadWorld(world, ctx.getArgument("save", Boolean.class)).whenComplete((success, throwable) ->{
                                        if(throwable != null) Crux.log(Level.SEVERE, throwable.getMessage());
                                        if(!success){
                                            sender.sendMessage("Could not unload world, " + world.getName() + "...");
                                            return;
                                        }
                                        sender.sendMessage("Unloaded world, " + world.getName() + ".");
                                    });
                                    return 1;
                                })
                        )
                )
        ).then(
            Commands.literal("delete")
                .then(
                    Commands.argument("world", worldArg)
                        .executes(ctx ->{
                            CommandSender sender = getExecutor(ctx.getSource());
                            CruxWorld world = ctx.getArgument("world", CruxWorld.class);
                            sender.sendMessage("Deleting world " + world.getName() + "...");
                            worldManager.deleteWorld(world).whenComplete((success, throwable) ->{
                                if(throwable != null) Crux.log(Level.SEVERE, throwable.getMessage());
                                if(!success){
                                    sender.sendMessage("Could not delete world, " + world.getName() + "...");
                                    return;
                                }
                                sender.sendMessage("Deleted world, " + world.getName() + ".");
                            });
                            return 1;
                        })
                )
        ).then(
            Commands.literal("create")
                .then(
                    Commands.argument("type", CruxWorldArgs.WORLD_TYPE)
                        .then(
                            Commands.argument("name", StringArgumentType.string())
                                .executes(ctx ->{
                                    CommandSender sender = getExecutor(ctx.getSource());
                                    CruxWorldType type = ctx.getArgument("type", CruxWorldType.class);
                                    String name = ctx.getArgument("name", String.class);
                                    sender.sendMessage("Getting or creating world...");
                                    CruxWorld world = worldManager.getOrCreateWorld(type, name);
                                    if(world == null){
                                        sender.sendMessage("Could not get or create world, " + name + " from type, " + type.key() + ".");
                                        return 0;
                                    }
                                    sender.sendMessage("Got world " + world.getName() + " from type " + type.key() + "!");
                                    return 1;
                                })
                                .then(
                                    Commands.argument("overwrite", BoolArgumentType.bool())
                                        .executes(ctx ->{
                                            CommandSender sender = getExecutor(ctx.getSource());
                                            CruxWorldType type = ctx.getArgument("type", CruxWorldType.class);
                                            String name = ctx.getArgument("name", String.class);
                                            boolean overwrite = ctx.getArgument("overwrite", Boolean.class);
                                            if(!overwrite && worldManager.getWorld(name) != null){
                                                sender.sendMessage(name + " world already exists.");
                                                return 0;
                                            }
                                            sender.sendMessage("Creating world...");
                                            CruxWorld world = worldManager.getOrCreateWorld(type, name);
                                            if(world == null){
                                                sender.sendMessage("Could not get or create world, " + name + " from type, " + type.key() + ".");
                                                return 0;
                                            }
                                            sender.sendMessage("Got world " + world.getName() + " from type " + type.key() + "!");
                                            return 1;
                                        })
                                )
                        )
                )
        ).then(
            Commands.literal("load")
                .then(
                    Commands.argument("world", StringArgumentType.string())
                        .suggests((ctx, builder) ->{
                            for(String s : CruxWorldUtil.getWorldsFromContainer()){
                                if(worldManager.getWorld(s) != null) continue;
                                builder.suggest(s);
                            }
                            return builder.buildFuture();
                        })
                        .executes(ctx ->{
                            CommandSender sender = getExecutor(ctx.getSource());
                            String worldName = ctx.getArgument("world", String.class);
                            if(worldManager.getWorld(worldName) != null){
                                sender.sendMessage(worldName + " is already loaded.");
                                return 0;
                            }
                            sender.sendMessage("Loading world...");
                            worldManager.loadWorld(worldName).whenComplete((success, throwable) ->{
                                if(throwable != null) Crux.log(Level.SEVERE, throwable.getMessage());
                                if(success == null){
                                    sender.sendMessage("Could not load world, " + worldName + "...");
                                    return;
                                }
                                sender.sendMessage("Loaded world, " + worldName + ".");
                            });
                            return 1;
                        })
                )
        ).then(
            Commands.literal("tp")
                .then(
                    Commands.argument("world", worldArg)
                        .then(
                            Commands.argument("targets", ArgumentTypes.entities())
                                .executes(ctx ->{
                                    CommandSender sender = getExecutor(ctx.getSource());
                                    CruxWorld world = ctx.getArgument("world", CruxWorld.class);
                                    Collection<Entity> targets = ctx.getArgument("targets", EntitySelectorArgumentResolver.class)
                                        .resolve(ctx.getSource());
                                    Location spawn = world.toBukkitWorld().getSpawnLocation();
                                    for(Entity e : targets){
                                        e.teleportAsync(spawn);
                                        sender.sendMessage("Teleported " + e.getName() + " to " + world.getName() + ".");
                                    }
                                    return 1;
                                })
                        )
                )
        )
        ;
        return dispatcher.build();
    }

    public static @NotNull CommandSender getExecutor(@NotNull CommandSourceStack source){
        return Objects.requireNonNullElse(source.getExecutor(), source.getSender());
    }
}
