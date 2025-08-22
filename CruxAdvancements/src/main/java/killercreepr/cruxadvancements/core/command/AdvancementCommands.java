package killercreepr.cruxadvancements.core.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import killercreepr.crux.api.communication.Communicator;
import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxadvancements.api.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.api.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.api.advancement.manager.CruxAdvancementManager;
import killercreepr.cruxadvancements.api.advancement.objective.progress.ObjectiveProgression;
import killercreepr.cruxadvancements.api.advancement.progress.CruxAdvancementProgress;
import killercreepr.cruxadvancements.core.advancement.progress.ListAdvancementProgress;
import killercreepr.cruxadvancements.core.advancement.progress.NumberAdvancementProgress;
import killercreepr.cruxadvancements.core.command.argument.AdvancementArguments;
import killercreepr.cruxadvancements.core.command.argument.CruxAdvancementListResolver;
import killercreepr.cruxadvancements.core.command.argument.CruxAdvancementResolver;
import killercreepr.cruxadvancements.core.data.AdvancementPair;
import killercreepr.cruxadvancements.core.entity.memory.AdvancementHolder;
import killercreepr.cruxadvancements.core.registries.AdvancementRegistries;
import killercreepr.cruxconfig.config.bukkit.file.CruxJson;
import killercreepr.cruxconfig.config.common.element.FileObject;
import net.kyori.adventure.key.Key;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

    public static long getSecondsTookToComplete(UUID uuid, AdvancementPair pair){
        CruxJson file = AdvancementHolder.getDefaultSaveFile(uuid);
        if(!(file.getElement("time_started") instanceof FileObject a)){
            file.close();
            return 0;
        }
        file.close();
        Long timeStarted = a.getOrDefaultObject(Long.class, pair.getAdvancementKey().asString(), 0L);
        if(timeStarted <= 0L) return 0;
        CruxJson data = findFile(pair.getManagerKey(), uuid);
        if(data == null) return 0;
        if(!(data.getElement("values") instanceof FileObject values)){
            data.close();
            return 0;
        }
        data.close();
        if(!(values.get(pair.getAdvancementKey().asString()) instanceof FileObject advance)) return 0;
        if(!(advance.get("progress") instanceof FileObject progress)) return 0;
        Instant instant = data.fileRegistry().deserializeFromFile(Instant.class, progress.get("obtained"));
        if(instant == null) return 0;

        long difference = instant.toEpochMilli() - timeStarted;
        return difference / 1000L;
    }

    public static CruxJson findFile(Key manager, UUID uuid){
        for(CruxPlugin plugin : CruxRegistries.PLUGIN){
            CruxJson data = new CruxJson(plugin, "data/cruxadvancements/" + manager.asString().replace(":", "_") + "/" + uuid);
            if(!data.file().exists()) continue;
            return data;
        }
        return null;
    }

    public static LiteralCommandNode<CommandSourceStack> build(LiteralArgumentBuilder<CommandSourceStack> dispatcher,
                                                               LifecycleEventManager<?> lifecycle){
        dispatcher.then(
            Commands.literal("dev")
                .then(
                    Commands.literal("timecompleted")
                        .then(
                            Commands.argument("uuid", StringArgumentType.string())
                                .then(
                                    Commands.argument("advancement", AdvancementArguments.ADVANCEMENT_PAIR)
                                        .executes(ctx ->{
                                            var sender = getExecutor(ctx.getSource());
                                            UUID uuid = UUID.fromString(ctx.getArgument("uuid", String.class));
                                            var pair = ctx.getArgument("advancement", AdvancementPair.class);
                                            long seconds = getSecondsTookToComplete(uuid, pair);
                                            sender.sendMessage("Seconds took to complete: " + seconds);
                                            return 1;
                                        })
                                )
                        )
                )
        ).then(
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
                    Commands.argument("manager", AdvancementArguments.ADVANCEMENT_MANAGER)
                        .then(
                            Commands.argument("advancement", AdvancementArguments.ADVANCEMENT)
                                .then(
                                    Commands.argument("target", AdvancementArguments.ADVANCEMENT_KEY)
                                        .executes(ctx ->{
                                            CruxAdvancementManager<?> manager = ctx.getArgument("manager", CruxAdvancementManager.class);
                                            CruxAdvancement advancement = ctx.getArgument("advancement", CruxAdvancementResolver.class)
                                                .resolve(manager);
                                            return check(ctx.getSource(), ctx.getArgument("target", String.class), advancement);
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
                                ).then(
                                    Commands.literal("*")
                                        .executes(ctx ->{
                                            Collection<Player> targets = ctx.getArgument("targets", PlayerSelectorArgumentResolver.class)
                                                .resolve(ctx.getSource());
                                            CruxAdvancementManager<?> manager = ctx.getArgument("manager", CruxAdvancementManager.class);
                                            Collection<CruxAdvancement> advancements = AdvancementArguments.ADVANCEMENT_LIST.parse(new StringReader("*"))
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
                                ).then(
                                    Commands.literal("*")
                                        .executes(ctx ->{
                                            Collection<Player> targets = ctx.getArgument("targets", PlayerSelectorArgumentResolver.class)
                                                .resolve(ctx.getSource());
                                            CruxAdvancementManager<?> manager = ctx.getArgument("manager", CruxAdvancementManager.class);
                                            Collection<CruxAdvancement> advancements = AdvancementArguments.ADVANCEMENT_LIST.parse(new StringReader("*"))
                                                .resolve(manager);
                                            return revoke(ctx.getSource(), targets, manager, advancements);
                                        })
                                )
                        )
                )
        ).then(
            Commands.literal("advance")
                .then(
                    Commands.literal("revoke")
                        .then(
                            Commands.argument("targets", ArgumentTypes.players())
                                .then(
                                    Commands.argument("advancements", AdvancementArguments.ADVANCEMENT_PAIR)
                                        .executes(ctx ->{
                                            Collection<Player> targets = ctx.getArgument("targets", PlayerSelectorArgumentResolver.class)
                                                .resolve(ctx.getSource());
                                            var pair = ctx.getArgument("advancements", AdvancementPair.class);
                                            return revoke(ctx.getSource(), targets, pair.getManager(), List.of(pair.getAdvancement()));
                                        })
                                ).then(
                                    Commands.literal("*")
                                        .executes(ctx ->{
                                            Collection<Player> targets = ctx.getArgument("targets", PlayerSelectorArgumentResolver.class)
                                                .resolve(ctx.getSource());
                                            for(var manager : AdvancementRegistries.ADVANCEMENT_MANAGERS){
                                                revoke(ctx.getSource(), targets, manager, (Collection<CruxAdvancement>) manager.getAdvancements().values());
                                            }
                                            return 1;
                                        })
                                )
                        )
                ).then(
                    Commands.literal("grant")
                        .then(
                            Commands.argument("targets", ArgumentTypes.players())
                                .then(
                                    Commands.argument("advancements", AdvancementArguments.ADVANCEMENT_PAIR)
                                        .executes(ctx ->{
                                            Collection<Player> targets = ctx.getArgument("targets", PlayerSelectorArgumentResolver.class)
                                                .resolve(ctx.getSource());
                                            var pair = ctx.getArgument("advancements", AdvancementPair.class);
                                            return grant(ctx.getSource(), targets, pair.getManager(), List.of(pair.getAdvancement()));
                                        })
                                ).then(
                                    Commands.literal("*")
                                        .executes(ctx ->{
                                            Collection<Player> targets = ctx.getArgument("targets", PlayerSelectorArgumentResolver.class)
                                                .resolve(ctx.getSource());
                                            for(var manager : AdvancementRegistries.ADVANCEMENT_MANAGERS){
                                                grant(ctx.getSource(), targets, manager, (Collection<CruxAdvancement>) manager.getAdvancements().values());
                                            }
                                            return 1;
                                        })
                                )
                        )
                ).then(
                    Commands.literal("check")
                        .then(
                            Commands.argument("advancements", AdvancementArguments.ADVANCEMENT_PAIR)
                                .then(
                                    Commands.argument("target", AdvancementArguments.ADVANCEMENT_KEY)
                                        .executes(ctx ->{
                                            var pair = ctx.getArgument("advancements", AdvancementPair.class);
                                            return check(ctx.getSource(), ctx.getArgument("target", String.class), pair.getAdvancement());
                                        })
                                )
                        )
                ).then(
                    Commands.literal("track")
                        .then(
                            Commands.argument("targets", ArgumentTypes.players())
                                .then(
                                    Commands.argument("manager", AdvancementArguments.ADVANCEMENT_PAIR)
                                        .then(
                                            Commands.argument("advancement", AdvancementArguments.ADVANCEMENT)
                                                .executes(ctx ->{
                                                    Collection<Player> targets = ctx.getArgument("targets", PlayerSelectorArgumentResolver.class)
                                                        .resolve(ctx.getSource());
                                                    var advancement = ctx.getArgument("advancement", AdvancementPair.class);
                                                    return track(ctx.getSource(), targets, advancement.getManager(), advancement.getAdvancement());
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
                                                    var advancement = ctx.getArgument("advancement", AdvancementPair.class);
                                                    return untrack(ctx.getSource(), targets, advancement.getManager(), advancement.getAdvancement());
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
                )
        );/*.then(
            Commands.literal("max_tracked")
                .then(
                    Commands.argument("targets", ArgumentTypes.players())
                        .then(
                            Commands.literal("set")
                                .then(
                                    Commands.argument("value", IntegerArgumentType.integer())
                                        .executes(ctx ->{
                                            Collection<Player> targets = ctx.getArgument("targets", PlayerSelectorArgumentResolver.class)
                                                .resolve(ctx.getSource());
                                            int value = ctx.getArgument("value", Integer.class);
                                            for(Player p : targets){
                                                AdvancementHolder holder = EntityMemory.getOrCreateDataHolder(p, AdvancementHolder.class);
                                                if(holder == null) continue;
                                                holder.setMaxTrackedAdvancements(value);
                                                getExecutor(ctx.getSource()).sendMessage(
                                                    "Set max trackable advancements to " + holder.getMaxTrackedAdvancements() + " for " + p.getName() + "."
                                                );
                                            }
                                            return 1;
                                        })
                                )
                        ).then(
                            Commands.literal("add")
                                .then(
                                    Commands.argument("value", IntegerArgumentType.integer())
                                        .executes(ctx ->{
                                            Collection<Player> targets = ctx.getArgument("targets", PlayerSelectorArgumentResolver.class)
                                                .resolve(ctx.getSource());
                                            int value = ctx.getArgument("value", Integer.class);
                                            for(Player p : targets){
                                                AdvancementHolder holder = EntityMemory.getOrCreateDataHolder(p, AdvancementHolder.class);
                                                if(holder == null) continue;
                                                holder.setMaxTrackedAdvancements(holder.getMaxTrackedAdvancements()+value);
                                                getExecutor(ctx.getSource()).sendMessage(
                                                    "Set max trackable advancements to " + holder.getMaxTrackedAdvancements() + " for " + p.getName() + "."
                                                );
                                            }
                                            return 1;
                                        })
                                )
                        ).then(
                            Commands.literal("remove")
                                .then(
                                    Commands.argument("value", IntegerArgumentType.integer())
                                        .executes(ctx ->{
                                            Collection<Player> targets = ctx.getArgument("targets", PlayerSelectorArgumentResolver.class)
                                                .resolve(ctx.getSource());
                                            int value = ctx.getArgument("value", Integer.class);
                                            for(Player p : targets){
                                                AdvancementHolder holder = EntityMemory.getOrCreateDataHolder(p, AdvancementHolder.class);
                                                if(holder == null) continue;
                                                holder.setMaxTrackedAdvancements(holder.getMaxTrackedAdvancements()-value);
                                                getExecutor(ctx.getSource()).sendMessage(
                                                    "Set max trackable advancements to " + holder.getMaxTrackedAdvancements() + " for " + p.getName() + "."
                                                );
                                            }
                                            return 1;
                                        })
                                )
                        )
                )
        );*/
        return dispatcher.build();
    }

    public static @NotNull CommandSender getExecutor(@NotNull CommandSourceStack source){
        return Objects.requireNonNullElse(source.getExecutor(), source.getSender());
    }

    public static int check(@NotNull CommandSourceStack source, @NotNull String name,
                            @NotNull CruxAdvancement advancement){
        CommandSender sender = getExecutor(source);
        CruxAdvancementProgress progress = advancement.getProgressIfPresent(name);

        Communicator.chat("<blue>Advancement Progress: " + progress).use(sender);
        if(progress instanceof NumberAdvancementProgress p){
            Communicator.chat("  <aqua>Number -> " + p.getProgress() + "/" + p.getCriteriaMaxProgress()).use(sender);
        }else if(progress instanceof ListAdvancementProgress p){
            p.getProgressMap().forEach((key, value) ->{
                Communicator.chat("  <aqua>" + key + " -> " + value).use(sender);
            });
        }//else Communicator.chat()("  <aqua>Unsupported progress or not present " + progress).use(sender);

        if(advancement instanceof ObjectiveAdvancement objective){
            ObjectiveProgression oProgress = objective.getObjectiveProgressIfPresent(name);
            if(oProgress != null){
                Communicator.chat("<yellow>Objective Progression:").use(sender);
                oProgress.getProgressMap().forEach((key, value) ->{
                    Communicator.chat("  <gold>" + key + " -> "+ value).use(sender);
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
        Communicator.chat(advancements.size() + " advancements granted for " + targets.size() + " players.")
            .use(sender);
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
        Communicator.chat(advancements.size() + " advancements revoked from " + targets.size() + " players.")
            .use(sender);
        return 1;
    }
}
