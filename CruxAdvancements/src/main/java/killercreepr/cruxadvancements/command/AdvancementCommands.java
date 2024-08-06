package killercreepr.cruxadvancements.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import killercreepr.crux.data.communication.MsgContainer;
import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.cruxadvancements.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.advancement.objective.progress.ObjectiveProgression;
import killercreepr.cruxadvancements.advancement.progression.CruxAdvancementProgress;
import killercreepr.cruxadvancements.advancement.progression.ListAdvancementProgress;
import killercreepr.cruxadvancements.advancement.progression.NumberAdvancementProgress;
import killercreepr.cruxadvancements.command.argument.AdvancementArguments;
import killercreepr.cruxadvancements.command.argument.CruxAdvancementListResolver;
import killercreepr.cruxadvancements.command.argument.CruxAdvancementResolver;
import killercreepr.cruxadvancements.data.entity.AdvancementHolder;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class AdvancementCommands {
    public static void register(@NotNull CruxPlugin plugin){
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event ->{
            final Commands commands = event.registrar();
            LiteralCommandNode<CommandSourceStack> cmd = build(Commands.literal("cruxadvancement")
                    .requires(source -> source.getSender().hasPermission("cruxadvancements.cmds.cruxadvancement.use")),
                plugin.getLifecycleManager());
            commands.register(cmd, List.of("cquest", "cadvance"));
        });
    }

    public static LiteralCommandNode<CommandSourceStack> build(LiteralArgumentBuilder<CommandSourceStack> dispatcher,
                                                               LifecycleEventManager<?> lifecycle){
        dispatcher.then(
            Commands.literal("track")
                .then(
                    Commands.argument("targets", ArgumentTypes.players())
                        .then(
                            Commands.argument("manager", AdvancementArguments.ADVANCEMENT_MANAGER)
                                .then(
                                    Commands.argument("advancement", AdvancementArguments.ADVANCEMENT)
                                        .executes(ctx ->{
                                            Collection<Player> targets = ctx.getArgument("targets", PlayerSelectorArgumentResolver.class)
                                                .resolve(ctx.getSource());
                                            CruxAdvancementManager<?> manager = ctx.getArgument("manager", CruxAdvancementManager.class);
                                            CruxAdvancement advancement = ctx.getArgument("advancement", CruxAdvancementResolver.class)
                                                .resolve(manager);
                                            return track(ctx.getSource(), targets, manager, advancement);
                                        })
                                )
                        )
                )
        ).then(
            Commands.literal("untrack")
                .then(
                    Commands.argument("targets", ArgumentTypes.players())
                        .then(
                            Commands.argument("manager", AdvancementArguments.ADVANCEMENT_MANAGER)
                                .then(
                                    Commands.argument("advancement", AdvancementArguments.ADVANCEMENT)
                                        .executes(ctx ->{
                                            Collection<Player> targets = ctx.getArgument("targets", PlayerSelectorArgumentResolver.class)
                                                .resolve(ctx.getSource());
                                            CruxAdvancementManager<?> manager = ctx.getArgument("manager", CruxAdvancementManager.class);
                                            CruxAdvancement advancement = ctx.getArgument("advancement", CruxAdvancementResolver.class)
                                                .resolve(manager);
                                            return untrack(ctx.getSource(), targets, manager, advancement);
                                        })
                                )
                        )
                        .then(
                            Commands.literal("all")
                                .executes(ctx ->{
                                    Collection<Player> targets = ctx.getArgument("targets", PlayerSelectorArgumentResolver.class)
                                        .resolve(ctx.getSource());
                                    return untrackAll(ctx.getSource(), targets);
                                })
                        )
                )
        ).then(
            Commands.literal("check")
                .then(
                    Commands.argument("target", ArgumentTypes.player())
                        .then(
                            Commands.argument("manager", AdvancementArguments.ADVANCEMENT_MANAGER)
                                .then(
                                    Commands.argument("advancement", AdvancementArguments.ADVANCEMENT)
                                        .executes(ctx ->{
                                            List<Player> targets = ctx.getArgument("target", PlayerSelectorArgumentResolver.class)
                                                .resolve(ctx.getSource());
                                            CruxAdvancementManager<?> manager = ctx.getArgument("manager", CruxAdvancementManager.class);
                                            CruxAdvancement advancement = ctx.getArgument("advancement", CruxAdvancementResolver.class)
                                                .resolve(manager);
                                            return check(ctx.getSource(), targets.getFirst(), advancement);
                                        })
                                )
                        )
                )
        ).then(
            Commands.literal("grant")
                .then(
                    Commands.argument("targets", ArgumentTypes.players())
                        .then(
                            Commands.argument("manager", AdvancementArguments.ADVANCEMENT_MANAGER)
                                .then(
                                    Commands.argument("advancements", AdvancementArguments.ADVANCEMENT_LIST)
                                        .executes(ctx ->{
                                            Collection<Player> targets = ctx.getArgument("targets", PlayerSelectorArgumentResolver.class)
                                                .resolve(ctx.getSource());
                                            CruxAdvancementManager<?> manager = ctx.getArgument("manager", CruxAdvancementManager.class);
                                            Collection<CruxAdvancement> advancements = ctx.getArgument("advancements", CruxAdvancementListResolver.class)
                                                .resolve(manager);
                                            return grant(ctx.getSource(), targets, manager, advancements);
                                        })
                                )
                        )
                )
        ).then(
            Commands.literal("revoke")
                .then(
                    Commands.argument("targets", ArgumentTypes.players())
                        .then(
                            Commands.argument("manager", AdvancementArguments.ADVANCEMENT_MANAGER)
                                .then(
                                    Commands.argument("advancements", AdvancementArguments.ADVANCEMENT_LIST)
                                        .executes(ctx ->{
                                            Collection<Player> targets = ctx.getArgument("targets", PlayerSelectorArgumentResolver.class)
                                                .resolve(ctx.getSource());
                                            CruxAdvancementManager<?> manager = ctx.getArgument("manager", CruxAdvancementManager.class);
                                            Collection<CruxAdvancement> advancements = ctx.getArgument("advancements", CruxAdvancementListResolver.class)
                                                .resolve(manager);
                                            return revoke(ctx.getSource(), targets, manager, advancements);
                                        })
                                )
                        )
                )
        );
        return dispatcher.build();
    }

    public static @NotNull CommandSender getExecutor(@NotNull CommandSourceStack source){
        return Objects.requireNonNullElse(source.getExecutor(), source.getSender());
    }

    public static int check(@NotNull CommandSourceStack source, @NotNull Player player,
                            @NotNull CruxAdvancement advancement){
        CommandSender sender = getExecutor(source);
        CruxAdvancementProgress progress = advancement.getProgressIfPresent(player.getUniqueId());

        new MsgContainer("<blue>Advancement Progress: " + progress).use(sender);
        if(progress instanceof NumberAdvancementProgress p){
            new MsgContainer("  <aqua>Number -> " + p.getProgress() + "/" + p.getCriteriaMaxProgress()).use(sender);
        }else if(progress instanceof ListAdvancementProgress p){
            p.getProgressMap().forEach((key, value) ->{
                new MsgContainer("  <aqua>" + key + " -> " + value).use(sender);
            });
        }//else new MsgContainer("  <aqua>Unsupported progress or not present " + progress).use(sender);

        if(advancement instanceof ObjectiveAdvancement objective){
            ObjectiveProgression oProgress = objective.getObjectiveProgressIfPresent(player.getUniqueId());
            if(oProgress != null){
                new MsgContainer("<yellow>Objective Progression:").use(sender);
                oProgress.getProgressMap().forEach((key, value) ->{
                    new MsgContainer("  <gold>" + key + " -> "+ value).use(sender);
                });
            }
        }
        return 1;
    }


    public static int untrackAll(@NotNull CommandSourceStack source, @NotNull Collection<Player> targets){
        CommandSender sender = getExecutor(source);
        for(Player p : targets){
            AdvancementHolder data = EntityMemory.getOrCreateDataHolder(p, AdvancementHolder.class);
            if(data==null) continue;
            data.getAdvancementTracker().untrackAll();
            sender.sendMessage(p.getName() + " is no longer tracking any advancements.");
        }
        return 1;
    }

    public static int untrack(@NotNull CommandSourceStack source, @NotNull Collection<Player> targets, @NotNull CruxAdvancementManager<?> manager,
                            @NotNull CruxAdvancement advancement){
        CommandSender sender = getExecutor(source);
        for(Player p : targets){
            AdvancementHolder data = EntityMemory.getOrCreateDataHolder(p, AdvancementHolder.class);
            if(data==null) continue;
            data.getAdvancementTracker().untrack(manager, advancement);
            sender.sendMessage(p.getName() + " is no longer tracking " + advancement.key() + ".");
        }
        return 1;
    }

    public static int track(@NotNull CommandSourceStack source, @NotNull Collection<Player> targets, @NotNull CruxAdvancementManager<?> manager,
                            @NotNull CruxAdvancement advancement){
        CommandSender sender = getExecutor(source);
        for(Player p : targets){
            AdvancementHolder data = EntityMemory.getOrCreateDataHolder(p, AdvancementHolder.class);
            if(data==null) continue;
            data.getAdvancementTracker().track(manager, advancement);
            sender.sendMessage(p.getName() + " is now tracking " + advancement.key() + ".");
        }
        return 1;
    }

    public static int grant(@NotNull CommandSourceStack source, @NotNull Collection<Player> targets, @NotNull CruxAdvancementManager manager,
                            @NotNull Collection<CruxAdvancement> advancements){
        CommandSender sender = getExecutor(source);
        for(Player p : targets){
            for(CruxAdvancement a : advancements){
                manager.grantAdvancement(p, a);
            }
        }
        new MsgContainer(advancements.size() + " advancements granted for " + targets.size() + " players.");
        return 1;
    }

    public static int revoke(@NotNull CommandSourceStack source, @NotNull Collection<Player> targets, @NotNull CruxAdvancementManager manager,
                            @NotNull Collection<CruxAdvancement> advancements){
        CommandSender sender = getExecutor(source);
        for(Player p : targets){
            for(CruxAdvancement a : advancements){
                manager.revokeAdvancement(p, a);
            }
        }
        new MsgContainer(advancements.size() + " advancements revoked from " + targets.size() + " players.");
        return 1;
    }
}
