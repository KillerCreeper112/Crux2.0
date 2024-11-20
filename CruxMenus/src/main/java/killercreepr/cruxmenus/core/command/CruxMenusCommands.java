package killercreepr.cruxmenus.core.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.api.text.tags.TagsPrefixBuilder;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.cruxmenus.api.menu.holder.MenuHolder;
import killercreepr.cruxmenus.core.command.argument.CruxMenusArguments;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CruxMenusCommands {
    public static void register(@NotNull CruxPlugin plugin){
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event ->{
            final Commands commands = event.registrar();
            LiteralCommandNode<CommandSourceStack> cmd = build(Commands.literal("cruxmenus")
                .requires(source -> source.getSender().hasPermission("cruxmenus.cmds.cruxmenus.use")), plugin.getLifecycleManager());
            commands.register(cmd, List.of("cm", "cruxmenu", "cmenu"));
        });
    }

    public static LiteralCommandNode<CommandSourceStack> build(LiteralArgumentBuilder<CommandSourceStack> dispatcher,
                                                               LifecycleEventManager<?> manager){
        //cruxmenu open <player> <id> <amount>
        dispatcher.then(
            Commands.literal("open")
                .then(
                    Commands.argument("targets", ArgumentTypes.players())
                        .then(
                            Commands.argument("menu", CruxMenusArguments.menuHolder())
                                .executes(ctx -> open(
                                    ctx.getArgument("targets", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()),
                                    ctx.getArgument("menu", MenuHolder.class)
                                ))
                                .then(
                                    Commands.argument("selector_target", ArgumentTypes.entity())
                                        .executes(ctx -> open(
                                            ctx.getArgument("targets", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource()),
                                            ctx.getArgument("menu", MenuHolder.class),
                                            DataExchange.empty(), TagContainer.merged().hook(
                                                ctx.getArgument("selector_target",
                                                    EntitySelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst(),
                                                TagsPrefixBuilder.overwriteBase("target_")
                                            )
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

    public static int open(Collection<Player> pp, MenuHolder menu){
        pp.forEach(e -> open(e, menu));
        return 1;
    }

    public static int open(Player p, MenuHolder menu){
        menu.open(p);
        return 1;
    }

    public static int open(Collection<Player> pp, MenuHolder menu, DataExchange info, MergedTagContainer tags){
        pp.forEach(e -> open(e, menu, info, tags));
        return 1;
    }

    public static int open(Player p, MenuHolder menu, DataExchange info, MergedTagContainer tags){
        menu.open(p, info, tags);
        return 1;
    }
}
