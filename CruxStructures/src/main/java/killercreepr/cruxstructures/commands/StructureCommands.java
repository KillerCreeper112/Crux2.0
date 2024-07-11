package killercreepr.cruxstructures.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.util.CruxMath;
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
    public static void register(@NotNull CruxPlugin plugin){
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event ->{
            final Commands commands = event.registrar();
            LiteralCommandNode<CommandSourceStack> cmd = build(Commands.literal("cruxstructures")
                .requires(source -> source.getSender().hasPermission("cruxstructures.cmds.cruxstructures.use")), plugin.getLifecycleManager());
            commands.register(cmd, List.of("cruxstructure", "cstruc"));
        });
    }

    public static LiteralCommandNode<CommandSourceStack> build(LiteralArgumentBuilder<CommandSourceStack> dispatcher,
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
        )
        ;
        return dispatcher.build();
    }

    public static @NotNull CommandSender getExecutor(@NotNull CommandSourceStack source){
        return Objects.requireNonNullElse(source.getExecutor(), source.getSender());
    }
}
