package killercreepr.cruxstructures.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import killercreepr.crux.data.communication.MsgContainer;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.util.CruxMath;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxstructures.commands.argument.StructureArgs;
import killercreepr.cruxstructures.structure.Structure;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class StructureCommands {
    protected final @NotNull CruxPlugin plugin;
    public StructureCommands(@NotNull CruxPlugin plugin) {
        this.plugin = plugin;
    }

    public void register(){
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event ->{
            final Commands commands = event.registrar();
            LiteralCommandNode<CommandSourceStack> cmd = build(Commands.literal("cruxstructures")
                .requires(source -> source.getSender().hasPermission("cruxstructures.cmds.cruxstructures.use")), plugin.getLifecycleManager());
            commands.register(cmd, List.of("cruxstructure", "cstruc"));
        });
    }

    public LiteralCommandNode<CommandSourceStack> build(LiteralArgumentBuilder<CommandSourceStack> dispatcher,
                                                               LifecycleEventManager<?> manager){
        //give <player> <id> <amount>
        dispatcher.then(
            Commands.literal("place")
                .then(
                    Commands.argument("structure", StructureArgs.STRUCTURE)
                        .executes(ctx ->{
                            Structure structure = ctx.getArgument("structure", Structure.class);
                            CommandSender sender = getExecutor(ctx.getSource());

                            Location spawn;
                            if(sender instanceof BlockCommandSender s){
                                spawn = s.getBlock().getLocation();
                            }else if(sender instanceof Entity s){
                                spawn = s.getLocation();
                            }else return -1;

                            structure.place(spawn);
                            sender.sendMessage("Structure, '" + structure.key() + "' has been placed at " +
                                CruxMath.format(spawn.getX()) + ", " + CruxMath.format(spawn.getY()) + ", " + CruxMath.format(spawn.getZ()) + " in world, " +
                                spawn.getWorld().getName() + ".");
                            return 1;
                        })
                        .then(
                            Commands.argument("location", ArgumentTypes.blockPosition())
                                .executes(ctx ->{
                                    Structure structure = ctx.getArgument("structure", Structure.class);
                                    BlockPosition position = ctx.getArgument("location", BlockPositionResolver.class)
                                        .resolve(ctx.getSource());
                                    CommandSender sender = getExecutor(ctx.getSource());

                                    World world;
                                    if(sender instanceof BlockCommandSender s){
                                        world = s.getBlock().getWorld();
                                    }else if(sender instanceof Entity s){
                                        world = s.getWorld();
                                    }else return -1;

                                    Location spawn = new Location(
                                        world, position.x(), position.y(), position.z()
                                    );
                                    structure.place(spawn);
                                    sender.sendMessage("Structure, '" + structure.key() + "' has been placed at " +
                                        CruxMath.format(spawn.getX()) + ", " + CruxMath.format(spawn.getY()) + ", " + CruxMath.format(spawn.getZ()) + " in world, " +
                                        spawn.getWorld().getName() + ".");
                                    return 1;
                                })
                        )
                )
        ).then(
            Commands.literal("create")
                .then(
                    Commands.argument("id", StringArgumentType.word())
                        .then(
                            Commands.argument("schematic", StringArgumentType.word())
                                .executes(ctx -> create(
                                    ctx.getSource(), ctx.getArgument("id", String.class),
                                    ctx.getArgument("schematic", String.class),
                                    false, null
                                ))
                                .then(
                                    Commands.argument("persists", BoolArgumentType.bool())
                                        .executes(ctx -> create(
                                            ctx.getSource(), ctx.getArgument("id", String.class),
                                            ctx.getArgument("schematic", String.class),
                                            ctx.getArgument("perissts", Boolean.class), null
                                        ))
                                        .then(
                                            Commands.argument("type", StringArgumentType.word())
                                                .executes(ctx -> create(
                                                    ctx.getSource(), ctx.getArgument("id", String.class),
                                                    ctx.getArgument("schematic", String.class),
                                                    ctx.getArgument("perissts", Boolean.class),
                                                    ctx.getArgument("type", String.class)
                                                ))
                                        )
                                )
                        )
                )
        )
        ;
        return dispatcher.build();
    }

    public static @NotNull CommandSender getExecutor(@NotNull CommandSourceStack source){
        return Objects.requireNonNullElse(source.getExecutor(), source.getSender());
    }

    public int create(@NotNull CommandSourceStack source, String id, String schematic, boolean persists, String type){
        CommandSender sender = getExecutor(source);
        CruxConfig cfg = new CruxConfig(plugin, "structures/" + id + ".yml");
        cfg.set("id", id);
        cfg.set("schematic", schematic);
        cfg.set("persists", persists);
        cfg.set("type", type);
        cfg.save();
        new MsgContainer("<green>Structure " + id + ", created!").use(sender);
        return 1;
    }
}
