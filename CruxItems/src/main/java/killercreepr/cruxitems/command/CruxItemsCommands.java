package killercreepr.cruxitems.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import killercreepr.crux.data.MsgContainer;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.util.CruxMath;
import killercreepr.cruxitems.command.argument.CruxItemsArguments;
import killercreepr.cruxitems.item.plugin.PluginItem;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CruxItemsCommands {
    public static void register(@NotNull CruxPlugin plugin){
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event ->{
            final Commands commands = event.registrar();
            LiteralCommandNode<CommandSourceStack> cmd = build(Commands.literal("cruxitems")
                .requires(source -> source.getSender().hasPermission("cruxitems.cmds.cruxitems.use")), plugin.getLifecycleManager());
            commands.register(cmd, List.of("ci", "cruxitem", "citem"));
        });
    }

    public static LiteralCommandNode<CommandSourceStack> build(LiteralArgumentBuilder<CommandSourceStack> dispatcher,
                                                               LifecycleEventManager<?> manager){
        //give <player> <id> <amount>
        dispatcher.then(
            Commands.literal("give")
                .then(
                    Commands.argument("targets", ArgumentTypes.entities())
                        .then(
                            Commands.argument("item", CruxItemsArguments.pluginItem())
                                .executes(ctx -> give(
                                    ctx.getSource(),
                                    ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                    ctx.getArgument("item", PluginItem.class), 1
                                ))
                                .then(
                                    Commands.argument("amount", IntegerArgumentType.integer(1, Integer.MAX_VALUE))
                                        .executes(ctx -> give(
                                            ctx.getSource(),
                                            ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                            ctx.getArgument("item", PluginItem.class),
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

    public static int give(@NotNull CommandSourceStack source, @NotNull Collection<Entity> targets, @NotNull PluginItem item, int amount){
        int given = 0;
        for(Entity entity : targets){
            if(!(entity instanceof LivingEntity e)) continue;

            EntityEquipment equip = e.getEquipment();
            if(equip == null) continue;

            ItemStack give = item.buildItem(entity);
            give.setAmount(amount);

            if(e instanceof Player p){
                for(ItemStack drop : p.getInventory().addItem(give).values()){
                    p.getWorld().dropItem(p.getLocation(), drop);
                }
                given++;
                continue;
            }

            equip.setItemInMainHand(give);
            given++;
        }
        int result = given > 0 ? 1 : -1;
        new MsgContainer("<yellow>Gave " + item.key() + " to " + CruxMath.format(given) + " entities.").use(getExecutor(source));
        return result;
    }
}
