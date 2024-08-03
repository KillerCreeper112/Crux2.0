package killercreepr.cruxadvancements.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.cruxadvancements.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.command.argument.AdvancementArguments;
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
        );
        return dispatcher.build();
    }

    public static @NotNull CommandSender getExecutor(@NotNull CommandSourceStack source){
        return Objects.requireNonNullElse(source.getExecutor(), source.getSender());
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
}
