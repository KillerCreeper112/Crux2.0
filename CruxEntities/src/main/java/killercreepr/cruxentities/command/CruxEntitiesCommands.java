package killercreepr.cruxentities.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import killercreepr.crux.data.MsgContainer;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.util.CruxMath;
import killercreepr.cruxentities.command.argument.CruxEntitiesArguments;
import killercreepr.cruxentities.entity.CruxMob;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class CruxEntitiesCommands {
    public static void register(@NotNull CruxPlugin plugin){
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event ->{
            final Commands commands = event.registrar();
            LiteralCommandNode<CommandSourceStack> cmd = build(Commands.literal("cruxentities")
                .requires(source -> source.getSender().hasPermission("cruxentities.cmds.cruxentities.use")), plugin.getLifecycleManager());
            commands.register(cmd, List.of("ce", "cruxentity", "centity"));
        });
    }

    public static LiteralCommandNode<CommandSourceStack> build(LiteralArgumentBuilder<CommandSourceStack> dispatcher,
                                                               LifecycleEventManager<?> manager){
        //spawn <entity> <pos>
        dispatcher.then(
            Commands.literal("spawn")
                .then(
                    Commands.argument("entity", CruxEntitiesArguments.cruxMob())
                        .executes(ctx -> spawn(
                            ctx.getSource(),
                            ctx.getArgument("entity", CruxMob.class),
                            ctx.getSource().getLocation(),
                            1
                        ))
                        .then(
                            Commands.argument("location", ArgumentTypes.blockPosition())
                                .executes(ctx -> spawn(
                                    ctx.getSource(),
                                    ctx.getArgument("entity", CruxMob.class),
                                    ctx.getArgument("location", BlockPositionResolver.class).resolve(ctx.getSource()),
                                    1
                                ))
                                .then(
                                    Commands.argument("amount", IntegerArgumentType.integer(1))
                                        .executes(ctx -> spawn(
                                            ctx.getSource(),
                                            ctx.getArgument("entity", CruxMob.class),
                                            ctx.getArgument("location", BlockPositionResolver.class).resolve(ctx.getSource()),
                                            ctx.getArgument("amount", Integer.class)
                                        ))
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

    public static int spawn(@NotNull CommandSourceStack source, @NotNull CruxMob mob, @NotNull BlockPosition location, int amount){
        Location spawn = location.toLocation(source.getLocation().getWorld());
        return spawn(source, mob, spawn, amount);
    }

    public static int spawn(@NotNull CommandSourceStack source, @NotNull CruxMob mob, @NotNull Location spawn, int amount){
        for(int i = amount; i > 0; i--){
            i--;
            mob.spawn(spawn);
        }
        new MsgContainer("Spawned " + amount + " " + mob.getName() + "'s at " +
            CruxMath.format(spawn.getX()) + ", " + CruxMath.format(spawn.getY()) + ", " + CruxMath.format(spawn.getZ()))
            .use(getExecutor(source));
        return 1;
    }
}
