package killercreepr.cruxform.core.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.math.FinePosition;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.math.CruxLocation;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.cruxform.api.scheduler.ShapeScheduler;
import killercreepr.cruxform.api.shape.CreateShape;
import killercreepr.cruxform.api.shape.CreateSwirl;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class CruxFormCommands {
    //
    public static void register(@NotNull CruxPlugin plugin){
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event ->{
            final Commands commands = event.registrar();
            LiteralCommandNode<CommandSourceStack> cmd = build(Commands.literal("cruxform")
                .requires(source -> source.getSender().hasPermission("crux.cmds.cruxform.use")),
                plugin.getLifecycleManager());
            commands.register(cmd, List.of("cform"));
        });
    }

    public static LiteralCommandNode<CommandSourceStack> build(LiteralArgumentBuilder<CommandSourceStack> dispatcher,
                                                                  LifecycleEventManager<?> manager){
        ///effect give killercreepr minecraft:absorption 1 2 true
        //cruxpotion give <player> <potion> <duration> <amplifier> <hide_particles>
        dispatcher.then(
            Commands.literal("shape")
                .then(
                    Commands.argument("pos", ArgumentTypes.finePosition())
                        .then(
                            Commands.argument("time", IntegerArgumentType.integer())
                                .then(
                                    Commands.argument("cache", BoolArgumentType.bool())
                                        .then(

                                            Commands.literal("swirl")
                                                .then(
                                                    Commands.argument("radius", DoubleArgumentType.doubleArg())
                                                        .then(
                                                            Commands.argument("height", DoubleArgumentType.doubleArg())
                                                                .then(
                                                                    Commands.argument("spacing", DoubleArgumentType.doubleArg())
                                                                        .then(
                                                                            Commands.argument("turns", DoubleArgumentType.doubleArg())
                                                                                .executes(ctx ->{
                                                                                    return performShape(
                                                                                        ctx,
                                                                                        CreateSwirl.builder()
                                                                                            .center(holder(ctx))
                                                                                            .radius(ctx.getArgument("radius", Double.class))
                                                                                            .height(ctx.getArgument("height", Double.class))
                                                                                            .spacing(ctx.getArgument("spacing", Double.class))
                                                                                            .turns(ctx.getArgument("turns", Double.class))
                                                                                            .build()
                                                                                    );
                                                                                })
                                                                        )
                                                                )
                                                        )
                                                )

                                        )
                                )
                        )
                )
        )
        ;
        return dispatcher.build();
    }

    public static Holder<CruxLocation> holder(CommandContext<CommandSourceStack> ctx){
        var sender = getExecutor(ctx.getSource());
        return () ->{
            Location loc = location(ctx.getArgument(
                "pos", FinePositionResolver.class
            ).resolve(ctx.getSource()), sender);
            return CruxLocation.location(loc);
        };
    }

    public static int performShape(CommandContext<CommandSourceStack> ctx, CreateShape shape) throws CommandSyntaxException {
        var sender = getExecutor(ctx.getSource());
        Location loc = location(ctx.getArgument(
            "pos", FinePositionResolver.class
        ).resolve(ctx.getSource()), sender);

        var builder = ShapeScheduler.builder()
            .shape(shape)
            .locationTick(c ->{
                Location l = c.getLocation().toLocation(loc.getWorld());
                l.getWorld().spawnParticle(Particle.FLAME, l, 0);
            });

        int ticks = ctx.getArgument("time", Integer.class);
        if(ctx.getArgument("cache", Boolean.class)){
            builder.buildCached().scheduleAsync(ticks);
        }else builder.build().schedule(ticks);
        sender.sendMessage("Scheduled shape " + shape.getClass().getSimpleName());
        return 1;
    }

    public static Location location(FinePosition pos, CommandSender sender){
        World world;
        if(sender instanceof Entity e){
            world = e.getWorld();
        }else if(sender instanceof BlockCommandSender s){
            world = s.getBlock().getWorld();
        }else world = Crux.getServer().getWorld(Key.key("overworld"));
        return pos.toLocation(world);
    }

    public static @NotNull CommandSender getExecutor(@NotNull CommandSourceStack source){
        return Objects.requireNonNullElse(source.getExecutor(), source.getSender());
    }
}
