package killercreepr.cruxstats.core.command;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import killercreepr.crux.core.command.argument.CruxCmdArguments;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.cruxstats.api.stat.*;
import killercreepr.cruxstats.core.command.argument.StatHolderResolver;
import killercreepr.cruxstats.core.command.argument.StatsArgs;
import net.kyori.adventure.key.Key;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CruxStatsCommands {
    public static void register(@NotNull CruxPlugin plugin){
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event ->{
            final Commands commands = event.registrar();
            LiteralCommandNode<CommandSourceStack> cmd = build(Commands.literal("cruxstats")
                .requires(source -> source.getSender().hasPermission("cruxstats.cmds.cruxstats.use")),
                plugin.getLifecycleManager());
            commands.register(cmd, List.of("cstat"));
        });
    }

    public static LiteralCommandNode<CommandSourceStack> build(LiteralArgumentBuilder<CommandSourceStack> dispatcher,
                                                                  LifecycleEventManager<?> manager){
        dispatcher.then(
            Commands.literal("add")
                .then(
                    Commands.argument("target", StatsArgs.STAT_HOLDER)
                        .then(
                            Commands.argument("stat", StatsArgs.STAT)
                                .then(
                                    Commands.argument("mod_key", CruxCmdArguments.CRUX_KEY)
                                        .then(
                                            Commands.argument("mod_amount", DoubleArgumentType.doubleArg())
                                                .executes(ctx ->{
                                                    CommandSourceStack source = ctx.getSource();
                                                    CommandSender sender = getExecutor(source);
                                                    CruxStatHolder holder = ctx.getArgument("target", StatHolderResolver.class)
                                                        .resolve(source);
                                                    CruxStat stat = ctx.getArgument("stat", CruxStat.class);
                                                    Key key = ctx.getArgument("mod_key", Key.class);
                                                    double amount = ctx.getArgument("mod_amount", Double.class);

                                                    CruxStatModifier mod = CruxStatModifier.addModifier(key, amount);
                                                    holder.getOrLoadStat(stat).addModifier(mod);
                                                    String name;
                                                    if(holder instanceof EntityStatHolder s) name = s.getName();
                                                    else name = "unknown";
                                                    sender.sendMessage("Added " + key + " modifier to " + name + " on stat " + stat.key() + " with amount: " + amount + ", operation: " + CruxStat.Operation.ADD.toString().toLowerCase());
                                                    return 1;
                                                })
                                                .then(
                                                    Commands.argument("mod_operation", StatsArgs.STAT_OPERATION)
                                                        .executes(ctx ->{
                                                            CommandSourceStack source = ctx.getSource();
                                                            CommandSender sender = getExecutor(source);
                                                            CruxStatHolder holder = ctx.getArgument("target", StatHolderResolver.class)
                                                                .resolve(source);
                                                            CruxStat stat = ctx.getArgument("stat", CruxStat.class);
                                                            Key key = ctx.getArgument("mod_key", Key.class);
                                                            double amount = ctx.getArgument("mod_amount", Double.class);
                                                            CruxStat.Operation operation = ctx.getArgument("mod_operation", CruxStat.Operation.class);

                                                            CruxStatModifier mod = CruxStatModifier.modifier(key, amount, operation);
                                                            holder.getOrLoadStat(stat).addModifier(mod);
                                                            String name;
                                                            if(holder instanceof EntityStatHolder s) name = s.getName();
                                                            else name = "unknown";
                                                            sender.sendMessage("Added " + key + " modifier to " + name + " on stat " + stat.key() + " with amount: " + amount + ", operation: " + operation.toString().toLowerCase());
                                                            return 1;
                                                        })
                                                )
                                        )
                                )
                        )
                )
        ).then(
            Commands.literal("remove")
                .then(
                    Commands.argument("target", StatsArgs.STAT_HOLDER)
                        .then(
                            Commands.argument("stat", StatsArgs.STAT)
                                .then(
                                    Commands.argument("mod_key", CruxCmdArguments.CRUX_KEY)
                                        .executes(ctx ->{
                                            CommandSourceStack source = ctx.getSource();
                                            CommandSender sender = getExecutor(source);
                                            CruxStatHolder holder = ctx.getArgument("target", StatHolderResolver.class)
                                                .resolve(source);
                                            CruxStat stat = ctx.getArgument("stat", CruxStat.class);
                                            Key key = ctx.getArgument("mod_key", Key.class);

                                            String name;
                                            if(holder instanceof EntityStatHolder s) name = s.getName();
                                            else name = "unknown";

                                            CruxStatInstance instance = holder.getStat(stat);
                                            if(instance == null){
                                                sender.sendMessage("Could not remove modifier, " + key + " from stat " + stat.key() + " on " + name + " because no instance exists.");
                                                return 0;
                                            }
                                            CruxStatModifier mod = instance.removeModifier(key);
                                            if(mod == null){
                                                sender.sendMessage("Could not remove modifier, " + key + " from stat " + stat.key() + " on " + name + " because modifier not present.");
                                                return 0;
                                            }

                                            sender.sendMessage("Removed " + key + " modifier from " + name + " on stat " + stat.key() + ". Modifier removed -> amount: " + mod.getAmount() + ", operation: " + mod.getOperation().toString().toLowerCase());
                                            return 1;
                                        })
                                )
                        )
                )
        ).then(
            Commands.literal("clear")
                .then(
                    Commands.argument("target", StatsArgs.STAT_HOLDER)
                        .then(
                            Commands.argument("stat", StatsArgs.STAT)
                                .executes(ctx ->{
                                    CommandSourceStack source = ctx.getSource();
                                    CommandSender sender = getExecutor(source);
                                    CruxStatHolder holder = ctx.getArgument("target", StatHolderResolver.class)
                                        .resolve(source);
                                    CruxStat stat = ctx.getArgument("stat", CruxStat.class);

                                    String name;
                                    if(holder instanceof EntityStatHolder s) name = s.getName();
                                    else name = "unknown";

                                    CruxStatInstance instance = holder.getStat(stat);
                                    if(instance == null){
                                        sender.sendMessage("Could not clear modifiers from stat " + stat.key() + " on " + name + " because no instance exists.");
                                        return 0;
                                    }
                                    Collection<CruxStatModifier> removed = instance.clearModifiers();
                                    sender.sendMessage("Cleared " + removed.size() + " modifiers from " + name + " on stat " + stat.key() + ".");
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
