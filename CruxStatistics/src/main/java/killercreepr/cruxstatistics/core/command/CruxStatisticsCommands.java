package killercreepr.cruxstatistics.core.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.SelectorArgumentResolver;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.cruxstatistics.api.bukkit.BukkitStatisticHolder;
import killercreepr.cruxstatistics.api.statistic.CruxStatisticType;
import killercreepr.cruxstatistics.core.command.argument.StatCmdArgs;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CruxStatisticsCommands {
    public static void register(@NotNull CruxPlugin plugin){
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event ->{
            final Commands commands = event.registrar();
            LiteralCommandNode<CommandSourceStack> cmd = build(Commands.literal("cruxstatistic")
                    .requires(source -> source.getSender().hasPermission("crux.cmds.cruxstatistic.use")),
                plugin.getLifecycleManager());
            commands.register(cmd, List.of("cstatistic"));
        });
    }

    public static LiteralCommandNode<CommandSourceStack> build(LiteralArgumentBuilder<CommandSourceStack> dispatcher, LifecycleEventManager<?> manager){
        return dispatcher.then(
            Commands.argument("target", ArgumentTypes.entities())
                .then(
                    Commands.literal("add")
                        .then(
                            Commands.argument("type", StatCmdArgs.CRUX_STATISTIC_TYPE)
                                .then(
                                    Commands.argument("amount", IntegerArgumentType.integer())
                                        .executes(ctx ->{
                                            CommandSender sender = getExecutor(ctx.getSource());
                                            Collection<Entity> targets = ctx.getArgument("target", EntitySelectorArgumentResolver.class)
                                                .resolve(ctx.getSource());
                                            CruxStatisticType<?> type = ctx.getArgument("type", CruxStatisticType.class);
                                            int amount = ctx.getArgument("amount", Integer.class);
                                            for(Entity e : targets){
                                                var holder = BukkitStatisticHolder.statisticHolder(e);
                                                if(holder == null){
                                                    sender.sendMessage(e.getName() + " is not a statistic holder.");
                                                    continue;
                                                }
                                                holder.incrementStatistic(type, amount);
                                                sender.sendMessage("Added " + amount + " to " + e.getName() + "'s " + type.key() + " statistic. They are now at " + holder.getStatistic(type));
                                            }
                                            return 1;
                                        })
                                )
                        )
                ).then(
                    Commands.literal("remove")
                        .then(
                            Commands.argument("type", StatCmdArgs.CRUX_STATISTIC_TYPE)
                                .then(
                                    Commands.argument("amount", IntegerArgumentType.integer())
                                        .executes(ctx ->{
                                            CommandSender sender = getExecutor(ctx.getSource());
                                            Collection<Entity> targets = ctx.getArgument("target", EntitySelectorArgumentResolver.class)
                                                .resolve(ctx.getSource());
                                            CruxStatisticType<?> type = ctx.getArgument("type", CruxStatisticType.class);
                                            int amount = ctx.getArgument("amount", Integer.class);
                                            for(Entity e : targets){
                                                var holder = BukkitStatisticHolder.statisticHolder(e);
                                                if(holder == null){
                                                    sender.sendMessage(e.getName() + " is not a statistic holder.");
                                                    continue;
                                                }
                                                holder.decrementStatistic(type, amount);
                                                sender.sendMessage("Removed " + amount + " to " + e.getName() + "'s " + type.key() + " statistic. They are now at " + holder.getStatistic(type));
                                            }
                                            return 1;
                                        })
                                )
                        )
                ).then(
                    Commands.literal("set")
                        .then(
                            Commands.argument("type", StatCmdArgs.CRUX_STATISTIC_TYPE)
                                .then(
                                    Commands.argument("amount", IntegerArgumentType.integer())
                                        .executes(ctx ->{
                                            CommandSender sender = getExecutor(ctx.getSource());
                                            Collection<Entity> targets = ctx.getArgument("target", EntitySelectorArgumentResolver.class)
                                                .resolve(ctx.getSource());
                                            CruxStatisticType<?> type = ctx.getArgument("type", CruxStatisticType.class);
                                            int amount = ctx.getArgument("amount", Integer.class);
                                            for(Entity e : targets){
                                                var holder = BukkitStatisticHolder.statisticHolder(e);
                                                if(holder == null){
                                                    sender.sendMessage(e.getName() + " is not a statistic holder.");
                                                    continue;
                                                }
                                                holder.setStatistic(type, amount);
                                                sender.sendMessage(e.getName() + "'s " + type.key() + " statistic set at " + holder.getStatistic(type));
                                            }
                                            return 1;
                                        })
                                )
                        )
                ).then(
                    Commands.literal("query")
                        .then(
                            Commands.argument("type", StatCmdArgs.CRUX_STATISTIC_TYPE)
                                .executes(ctx ->{
                                    CommandSender sender = getExecutor(ctx.getSource());
                                    Collection<Entity> targets = ctx.getArgument("target", EntitySelectorArgumentResolver.class)
                                        .resolve(ctx.getSource());
                                    CruxStatisticType<?> type = ctx.getArgument("type", CruxStatisticType.class);
                                    for(Entity e : targets){
                                        var holder = BukkitStatisticHolder.statisticHolder(e);
                                        if(holder == null){
                                            sender.sendMessage(e.getName() + " is not a statistic holder.");
                                            continue;
                                        }
                                        sender.sendMessage(e.getName() + "'s " + type.key() + " statistic is: " + holder.getStatistic(type));
                                    }
                                    return 1;
                                })
                        )
                )
        ).build();
    }

    public static @NotNull CommandSender getExecutor(@NotNull CommandSourceStack source){
        return Objects.requireNonNullElse(source.getExecutor(), source.getSender());
    }
}
