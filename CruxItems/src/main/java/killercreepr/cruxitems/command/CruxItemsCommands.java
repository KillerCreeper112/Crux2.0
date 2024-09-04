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
import killercreepr.crux.command.argument.CruxCmdArguments;
import killercreepr.crux.data.communication.MsgContainer;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.util.CruxItem;
import killercreepr.crux.util.CruxMath;
import killercreepr.cruxitems.command.argument.CruxItemsArguments;
import killercreepr.cruxitems.item.plugin.PluginItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

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
        ).then(
            Commands.literal("item_name")
                .then(
                    Commands.argument("targets", ArgumentTypes.entities())
                        .executes(ctx -> itemName(
                            ctx.getSource(),
                            ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                            null
                        ))
                        .then(
                            Commands.argument("name", ArgumentTypes.component())
                                .executes(ctx -> itemName(
                                    ctx.getSource(),
                                    ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                    ctx.getArgument("name", Component.class)
                                ))
                        )
                )
        ).then(
            Commands.literal("custom_name")
                .then(
                    Commands.argument("targets", ArgumentTypes.entities())
                        .executes(ctx -> customName(
                            ctx.getSource(),
                            ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                            null
                        ))
                        .then(
                            Commands.argument("name", ArgumentTypes.component())
                                .executes(ctx -> customName(
                                    ctx.getSource(),
                                    ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                    ctx.getArgument("name", Component.class)
                                ))
                        )
                )
        ).then(
            Commands.literal("color")
                .then(
                    Commands.argument("targets", ArgumentTypes.entities())
                        .executes(ctx -> color(
                            ctx.getSource(),
                            ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                            null
                        ))
                        .then(
                            Commands.argument("color", CruxCmdArguments.COLOR)
                                .executes(ctx -> color(
                                    ctx.getSource(),
                                    ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                    ctx.getArgument("color", Color.class)
                                ))
                        )
                )
        ).then(
            Commands.literal("flags")
                .then(
                    Commands.argument("targets", ArgumentTypes.entities())
                        .then(
                            Commands.literal("add")
                                .then(
                                    Commands.argument("flag", CruxCmdArguments.ITEM_FLAG)
                                        .executes(ctx -> addFlag(
                                            ctx.getSource(),
                                            ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                            ctx.getArgument("flag", ItemFlag.class)
                                        ))
                                )
                        ).then(
                            Commands.literal("remove")
                                .then(
                                    Commands.argument("flag", CruxCmdArguments.ITEM_FLAG)
                                        .executes(ctx -> removeFlag(
                                            ctx.getSource(),
                                            ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                            ctx.getArgument("flag", ItemFlag.class)
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

    public static int addFlag(@NotNull CommandSourceStack source, @NotNull Collection<Entity> targets, @NotNull ItemFlag flag){
        int given = mainHandArgument(source, targets, item ->{
            item.addFlags(flag);
        });
        new MsgContainer("Added item flag, " + flag.toString().toLowerCase() + " to main hand items of " + given + " entities.").use(getExecutor(source));
        return given > 0 ? 1 : -1;
    }

    public static int removeFlag(@NotNull CommandSourceStack source, @NotNull Collection<Entity> targets, @NotNull ItemFlag flag){
        int given = mainHandArgument(source, targets, item ->{
            item.removeFlags(flag);
        });
        new MsgContainer("Removed item flag, " + flag.toString().toLowerCase() + " to main hand items of " + given + " entities.").use(getExecutor(source));
        return given > 0 ? 1 : -1;
    }

    public static int mainHandArgument(@NotNull CommandSourceStack source, @NotNull Collection<Entity> targets, @NotNull Consumer<CruxItem> mainHand){
        int given = 0;
        for(Entity entity : targets) {
            if (!(entity instanceof LivingEntity e)) continue;

            EntityEquipment equip = e.getEquipment();
            if (equip == null) continue;
            CruxItem item = new CruxItem(equip.getItemInMainHand());
            mainHand.accept(item);
            if(!(entity instanceof Player)){
                equip.setItemInMainHand(item.item(), true);
            }
            given++;
        }
        return given;
    }

    public static int color(@NotNull CommandSourceStack source, @NotNull Collection<Entity> targets, @Nullable Color color){
        int given = mainHandArgument(source, targets, item ->{
            item.color(color);
        });
        new MsgContainer("Changed the color of main hand items on " + given + " entities.").use(getExecutor(source));
        return given > 0 ? 1 : -1;
    }

    public static int itemName(@NotNull CommandSourceStack source, @NotNull Collection<Entity> targets, @Nullable Component name){
        int given = mainHandArgument(source, targets, item ->{
            item.itemName(name);
        });
        new MsgContainer("Changed the item name of main hand items on " + given + " entities.").use(getExecutor(source));
        return given > 0 ? 1 : -1;
    }

    public static int customName(@NotNull CommandSourceStack source, @NotNull Collection<Entity> targets, @Nullable Component name){
        int given = mainHandArgument(source, targets, item ->{
            item.customName(name);
        });
        new MsgContainer("Changed the custom name of main hand items on " + given + " entities.").use(getExecutor(source));
        return given > 0 ? 1 : -1;
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
        new MsgContainer("Gave " + item.key() + " to " + CruxMath.format(given) + " entities.").use(getExecutor(source));
        return result;
    }
}
