package killercreepr.cruxworlds.core.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.crux.core.util.CruxWorldUtil;
import killercreepr.cruxworlds.api.world.CruxWorld;
import killercreepr.cruxworlds.api.world.entity.NaturalEntitySpawnGroup;
import killercreepr.cruxworlds.api.world.entity.SpawnContext;
import killercreepr.cruxworlds.api.world.manager.CruxWorldManager;
import killercreepr.cruxworlds.api.world.type.CruxWorldType;
import killercreepr.cruxworlds.core.command.arguments.CruxWorldArgs;
import killercreepr.cruxworlds.core.command.arguments.CruxWorldArgument;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
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
                            sender.sendMessage("Unloading world " + world.key() + "...");
                            worldManager.unloadWorld(world, true).whenComplete((success, throwable) ->{
                                if(throwable != null) Crux.log(Level.SEVERE, throwable.getMessage());
                                if(!success){
                                    sender.sendMessage("Could not unload world, " + world.key() + "...");
                                    return;
                                }
                                sender.sendMessage("Unloaded world, " + world.key() + ".");
                            });
                            return 1;
                        })
                        .then(
                            Commands.argument("save", BoolArgumentType.bool())
                                .executes(ctx ->{
                                    CommandSender sender = getExecutor(ctx.getSource());
                                    CruxWorld world = ctx.getArgument("world", CruxWorld.class);
                                    sender.sendMessage("Unloading world " + world.key() + "...");
                                    worldManager.unloadWorld(world, ctx.getArgument("save", Boolean.class)).whenComplete((success, throwable) ->{
                                        if(throwable != null) Crux.log(Level.SEVERE, throwable.getMessage());
                                        if(!success){
                                            sender.sendMessage("Could not unload world, " + world.key() + "...");
                                            return;
                                        }
                                        sender.sendMessage("Unloaded world, " + world.key() + ".");
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
                            sender.sendMessage("Deleting world " + world.key() + "...");
                            worldManager.deleteWorld(world).whenComplete((success, throwable) ->{
                                if(throwable != null) Crux.log(Level.SEVERE, throwable.getMessage());
                                if(!success){
                                    sender.sendMessage("Could not delete world, " + world.key() + "...");
                                    return;
                                }
                                sender.sendMessage("Deleted world, " + world.key() + ".");
                            });
                            return 1;
                        })
                )
        ).then(
            Commands.literal("create")
                .then(
                    Commands.argument("type", CruxWorldArgs.WORLD_TYPE)
                        .executes(ctx ->{
                            CommandSender sender = getExecutor(ctx.getSource());
                            CruxWorldType type = ctx.getArgument("type", CruxWorldType.class);
                            Key name = type.defaultWorldKey();
                            sender.sendMessage("Getting or creating world...");
                            CruxWorld world = worldManager.getOrCreateWorld(type, name);
                            if(world == null){
                                sender.sendMessage("Could not get or create world, " + name + " from type, " + type.key() + ".");
                                return 0;
                            }
                            sender.sendMessage("Got world " + world.key() + " from type " + type.key() + "!");
                            return 1;
                        })
                        .then(
                            Commands.argument("name", ArgumentTypes.key())
                                .executes(ctx ->{
                                    CommandSender sender = getExecutor(ctx.getSource());
                                    CruxWorldType type = ctx.getArgument("type", CruxWorldType.class);
                                    Key name = ctx.getArgument("name", Key.class);
                                    sender.sendMessage("Getting or creating world...");
                                    CruxWorld world = worldManager.getOrCreateWorld(type, name);
                                    if(world == null){
                                        sender.sendMessage("Could not get or create world, " + name + " from type, " + type.key() + ".");
                                        return 0;
                                    }
                                    sender.sendMessage("Got world " + world.key() + " from type " + type.key() + "!");
                                    return 1;
                                })
                                .then(
                                    Commands.argument("overwrite", BoolArgumentType.bool())
                                        .executes(ctx ->{
                                            CommandSender sender = getExecutor(ctx.getSource());
                                            CruxWorldType type = ctx.getArgument("type", CruxWorldType.class);
                                            Key name = ctx.getArgument("name", Key.class);
                                            boolean overwrite = ctx.getArgument("overwrite", Boolean.class);
                                            if(!overwrite && worldManager.getWorld(name) != null){
                                                sender.sendMessage(name + " world already exists.");
                                                return 0;
                                            }
                                            if(overwrite){
                                                sender.sendMessage("Deleting previous world if exists...");
                                                worldManager.deleteWorld(name);
                                            }
                                            sender.sendMessage("Creating world...");
                                            CruxWorld world = worldManager.getOrCreateWorld(type, name);
                                            if(world == null){
                                                sender.sendMessage("Could not get or create world, " + name + " from type, " + type.key() + ".");
                                                return 0;
                                            }
                                            sender.sendMessage("Got world " + world.key() + " from type " + type.key() + "!");
                                            return 1;
                                        })
                                )
                              .then(
                                Commands.argument("tp", BoolArgumentType.bool())
                                  .executes(ctx ->{
                                    CommandSender sender = getExecutor(ctx.getSource());
                                    CruxWorldType type = ctx.getArgument("type", CruxWorldType.class);
                                    Key name = ctx.getArgument("name", Key.class);
                                    boolean overwrite = ctx.getArgument("overwrite", Boolean.class);
                                    boolean tp = ctx.getArgument("tp", Boolean.class);
                                    if(!overwrite && worldManager.getWorld(name) != null){
                                      sender.sendMessage(name + " world already exists.");
                                      return 0;
                                    }
                                    if(overwrite){
                                      sender.sendMessage("Deleting previous world if exists...");
                                      worldManager.deleteWorld(name);
                                    }
                                    sender.sendMessage("Creating world...");
                                    CruxWorld world = worldManager.getOrCreateWorld(type, name);
                                    if(world == null){
                                      sender.sendMessage("Could not get or create world, " + name + " from type, " + type.key() + ".");
                                      return 0;
                                    }
                                    sender.sendMessage("Got world " + world.key() + " from type " + type.key() + "!");
                                    if(tp){
                                      if(sender instanceof Entity e){
                                        e.teleport(world.toBukkitWorld().getSpawnLocation());
                                      }
                                    }
                                    return 1;
                                  })
                              )
                        )
                )
        ).then(
            Commands.literal("load")
                .then(
                    Commands.argument("world", ArgumentTypes.key())
                        .suggests((ctx, builder) ->{
                            for(String s : CruxWorldUtil.getWorldsFromContainer()){
                                Key key = Key.key(s);
                                if(worldManager.getWorld(key) != null) continue;
                                builder.suggest(key.asString());
                            }
                            return builder.buildFuture();
                        })
                        .executes(ctx ->{
                            CommandSender sender = getExecutor(ctx.getSource());
                            Key worldName = ctx.getArgument("world", Key.class);
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
                        .executes(ctx ->{
                            CommandSender sender = getExecutor(ctx.getSource());
                            if(!(sender instanceof Entity e)) return -1;
                            CruxWorld world = ctx.getArgument("world", CruxWorld.class);
                            Location spawn = world.toBukkitWorld().getSpawnLocation();
                            e.teleportAsync(spawn);
                            sender.sendMessage("Teleported " + e.getName() + " to " + world.key() + ".");
                            return 1;
                        })
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
                                        sender.sendMessage("Teleported " + e.getName() + " to " + world.key() + ".");
                                    }
                                    return 1;
                                })
                        )
                )
        ).then(
            Commands.literal("createworld")
                .then(
                    Commands.argument("name", StringArgumentType.word())
                        .then(
                            Commands.argument("environment", StringArgumentType.word())
                                .suggests((ct, b) ->{
                                    for(var e : World.Environment.values()){
                                        b.suggest(e.toString().toLowerCase());
                                    }
                                    return b.buildFuture();
                                })
                                .then(
                                    Commands.argument("type", StringArgumentType.word())
                                        .suggests((ct, b) ->{
                                            for(var e : WorldType.values()){
                                                b.suggest(e.toString().toLowerCase());
                                            }
                                            return b.buildFuture();
                                        })
                                        .executes(ctx ->{
                                            String name = ctx.getArgument("name", String.class);
                                            var env = World.Environment.valueOf(ctx.getArgument("environment", String.class).toUpperCase());
                                            var type = WorldType.valueOf(ctx.getArgument("type", String.class).toUpperCase());
                                            WorldCreator creator = new WorldCreator(name);
                                            creator.type(type).environment(env);
                                            getExecutor(ctx.getSource()).sendMessage("Creating world " + name + "...");
                                            creator.createWorld();
                                            return 1;
                                        })
                                        .then(
                                            Commands.argument("options", StringArgumentType.greedyString())
                                                .executes(ctx ->{
                                                    String name = ctx.getArgument("name", String.class);
                                                    var env = World.Environment.valueOf(ctx.getArgument("environment", String.class).toUpperCase());
                                                    var type = WorldType.valueOf(ctx.getArgument("type", String.class).toUpperCase());
                                                    WorldCreator creator = new WorldCreator(name);
                                                    creator.type(type).environment(env)
                                                        .generatorSettings(ctx.getArgument("options", String.class));
                                                    getExecutor(ctx.getSource()).sendMessage("Creating world " + name + "...");
                                                    creator.createWorld();
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )
        ).then(
            Commands.literal("entity")
                .then(
                    Commands.literal("spawn")
                        .then(
                            Commands.argument("entity_group", CruxWorldArgs.NATURAL_ENTITY_SPAWN_GROUP)
                                .executes(ctx ->{
                                    var sender = getExecutor(ctx.getSource());
                                    Location loc = getLocation(sender);
                                    if(loc == null){
                                        return -1;
                                    }
                                    var group = ctx.getArgument("entity_group", NaturalEntitySpawnGroup.class);
                                    SpawnContext spawnCtx = SpawnContext.simple(loc.getBlock(), CruxMath.random());
                                    group.selectRandom(1, spawnCtx).forEach(spawn ->{
                                        spawn.spawn(spawnCtx);
                                    });
                                    sender.sendMessage("Spawned 1 roll of mobs.");
                                    return 1;
                                })
                                .then(
                                    Commands.argument("position", ArgumentTypes.finePosition())
                                        .executes(ctx ->{
                                            var sender = getExecutor(ctx.getSource());
                                            Location loc = getLocation(sender);
                                            if(loc == null){
                                                return -1;
                                            }
                                            var pos = ctx.getArgument("position", FinePositionResolver.class)
                                                .resolve(ctx.getSource());
                                            loc = pos.toLocation(loc.getWorld());

                                            var group = ctx.getArgument("entity_group", NaturalEntitySpawnGroup.class);
                                            SpawnContext spawnCtx = SpawnContext.simple(loc.getBlock(), CruxMath.random());
                                            group.selectRandom(1, spawnCtx).forEach(spawn ->{
                                                spawn.spawn(spawnCtx);
                                            });
                                            sender.sendMessage("Spawned 1 roll of mobs.");
                                            return 1;
                                        })
                                        .then(
                                            Commands.argument("rolls", IntegerArgumentType.integer())
                                                .executes(ctx ->{
                                                    var sender = getExecutor(ctx.getSource());
                                                    Location loc = getLocation(sender);
                                                    if(loc == null){
                                                        return -1;
                                                    }
                                                    var pos = ctx.getArgument("position", FinePositionResolver.class)
                                                        .resolve(ctx.getSource());
                                                    loc = pos.toLocation(loc.getWorld());

                                                    int rolls = ctx.getArgument("rolls", Integer.class);
                                                    var group = ctx.getArgument("entity_group", NaturalEntitySpawnGroup.class);
                                                    SpawnContext spawnCtx = SpawnContext.simple(loc.getBlock(), CruxMath.random());
                                                    group.selectRandom(rolls, spawnCtx).forEach(spawn ->{
                                                        spawn.spawn(spawnCtx);
                                                    });
                                                    sender.sendMessage("Spawned " + rolls + " rolls of mobs.");
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )
        )
        ;
        return dispatcher.build();
    }

    public static Location getLocation(CommandSender sender){
        if(sender instanceof Entity e) return e.getLocation();
        if(sender instanceof BlockCommandSender s) return s.getBlock().getLocation().toCenterLocation();
        return null;
    }

    public static @NotNull CommandSender getExecutor(@NotNull CommandSourceStack source){
        return Objects.requireNonNullElse(source.getExecutor(), source.getSender());
    }
}
