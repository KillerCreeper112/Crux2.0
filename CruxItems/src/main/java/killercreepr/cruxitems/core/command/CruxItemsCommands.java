package killercreepr.cruxitems.core.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import killercreepr.crux.api.communication.Communicator;
import killercreepr.crux.api.component.DataComponentType;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.command.argument.CruxCmdArguments;
import killercreepr.crux.core.item.dynamic.component.DynamicItemCruxComponents;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxitems.api.item.plugin.PluginItem;
import killercreepr.cruxitems.core.command.argument.CruxItemsArguments;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
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
            Commands.literal("book")
                .then(
                    Commands.literal("open")
                        .then(
                            Commands.argument("targets", ArgumentTypes.players())
                                .then(
                                    Commands.argument("item", CruxItemsArguments.pluginItem())
                                        .executes(ctx ->{
                                            var sender = getExecutor(ctx.getSource());
                                            var targets = ctx.getArgument("targets", PlayerSelectorArgumentResolver.class)
                                                .resolve(ctx.getSource());
                                            var item = ctx.getArgument("item", PluginItem.class);
                                            if(!(item.buildItem().getItemMeta() instanceof BookMeta meta)){
                                                sender.sendMessage(item.key() + " is not a book.");
                                                return 0;
                                            }
                                            for(HumanEntity p : targets){
                                                p.openBook(meta);
                                            }
                                            sender.sendMessage("Opened " + item.key() + " book for " + targets.size() + " targets.");
                                            return 1;
                                        })
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
            Commands.literal("damage")
                .then(
                    Commands.argument("targets", ArgumentTypes.entities())
                        .executes(ctx -> damage(
                            ctx.getSource(),
                            ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                            null
                        ))
                        .then(
                            Commands.argument("damage", IntegerArgumentType.integer())
                                .executes(ctx -> damage(
                                    ctx.getSource(),
                                    ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                    ctx.getArgument("damage", Integer.class)
                                ))
                        )
                )
        ).then(
            Commands.literal("max_damage")
                .then(
                    Commands.argument("targets", ArgumentTypes.entities())
                        .executes(ctx -> maxDamage(
                            ctx.getSource(),
                            ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                            null
                        ))
                        .then(
                            Commands.argument("max_damage", IntegerArgumentType.integer())
                                .executes(ctx -> maxDamage(
                                    ctx.getSource(),
                                    ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                    ctx.getArgument("max_damage", Integer.class)
                                ))
                        )
                )
        ).then(
            Commands.literal("max_stack_size")
                .then(
                    Commands.argument("targets", ArgumentTypes.entities())
                        .executes(ctx -> maxStackSize(
                            ctx.getSource(),
                            ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                            null
                        ))
                        .then(
                            Commands.argument("max_stack_size", IntegerArgumentType.integer())
                                .executes(ctx -> maxStackSize(
                                    ctx.getSource(),
                                    ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                    ctx.getArgument("max_stack_size", Integer.class)
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
        ).then(
            Commands.literal("components")
                .then(
                    Commands.argument("targets", ArgumentTypes.entities())
                        .then(
                            Commands.literal("apply")
                                .then(
                                    Commands.argument("input", StringArgumentType.greedyString())
                                        .suggests((source, builder) ->{
                                            for (Map.Entry<Key, DataComponentType<?>> entry : CruxRegistries.DATA_COMPONENT_TYPE.entrySet()) {
                                                builder.suggest(Crux.keyMinimalString(entry.getKey()));
                                            }
                                            return builder.buildFuture();
                                        })
                                        .executes(ctx -> applyComponents(
                                            ctx.getSource(),
                                            ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                            ctx.getArgument("input", String.class)
                                        ))
                                )
                        ).then(
                            Commands.literal("clear")
                                .executes(ctx -> clearComponents(
                                    ctx.getSource(),
                                    ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource())
                                ))
                        ).then(
                            Commands.literal("remove")
                                .then(
                                    Commands.argument("input", StringArgumentType.greedyString())
                                        .suggests((source, builder) ->{
                                            for (Map.Entry<Key, DataComponentType<?>> entry : CruxRegistries.DATA_COMPONENT_TYPE.entrySet()) {
                                                builder.suggest(Crux.keyMinimalString(entry.getKey()));
                                            }
                                            return builder.buildFuture();
                                        })
                                        .executes(ctx -> removeComponents(
                                            ctx.getSource(),
                                            ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                            ctx.getArgument("input", String.class)
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
        Communicator.chat("Added item flag, " + flag.toString().toLowerCase() + " to main hand items of " + given + " entities.").use(getExecutor(source));
        return given > 0 ? 1 : -1;
    }

    public static int removeFlag(@NotNull CommandSourceStack source, @NotNull Collection<Entity> targets, @NotNull ItemFlag flag){
        int given = mainHandArgument(source, targets, item ->{
            item.removeFlags(flag);
        });
        Communicator.chat("Removed item flag, " + flag.toString().toLowerCase() + " to main hand items of " + given + " entities.").use(getExecutor(source));
        return given > 0 ? 1 : -1;
    }

    public static int mainHandArgument(@NotNull CommandSourceStack source, @NotNull Collection<Entity> targets, @NotNull Consumer<CruxItem> mainHand){
        int given = 0;
        for(Entity entity : targets) {
            if (!(entity instanceof LivingEntity e)) continue;

            EntityEquipment equip = e.getEquipment();
            if (equip == null) continue;
            CruxItem item = CruxItem.wrap(equip.getItemInMainHand());
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
        Communicator.chat("Changed the color of main hand items on " + given + " entities.").use(getExecutor(source));
        return given > 0 ? 1 : -1;
    }

    public static int damage(@NotNull CommandSourceStack source, @NotNull Collection<Entity> targets, @Nullable Integer dmg){
        int given = mainHandArgument(source, targets, item ->{
            if(dmg == null){
                item.item().resetData(DataComponentTypes.DAMAGE);
            }else{
                item.item().setData(DataComponentTypes.DAMAGE, dmg);
            }
        });
        Communicator.chat("Set the damage of main hand items on " + given + " entities.").use(getExecutor(source));
        return given > 0 ? 1 : -1;
    }

    public static int maxDamage(@NotNull CommandSourceStack source, @NotNull Collection<Entity> targets, @Nullable Integer dmg){
        int given = mainHandArgument(source, targets, item ->{
            if(dmg == null){
                item.item().resetData(DataComponentTypes.MAX_DAMAGE);
            }else{
                item.item().setData(DataComponentTypes.MAX_DAMAGE, dmg);
            }
        });
        Communicator.chat("Set the max damage of main hand items on " + given + " entities.").use(getExecutor(source));
        return given > 0 ? 1 : -1;
    }

    public static int maxStackSize(@NotNull CommandSourceStack source, @NotNull Collection<Entity> targets, @Nullable Integer dmg){
        int given = mainHandArgument(source, targets, item ->{
            if(dmg == null){
                item.item().resetData(DataComponentTypes.MAX_STACK_SIZE);
            }else{
                item.item().setData(DataComponentTypes.MAX_STACK_SIZE, dmg);
            }
        });
        Communicator.chat("Set the max stack size of main hand items on " + given + " entities.").use(getExecutor(source));
        return given > 0 ? 1 : -1;
    }

    public static int removeComponents(@NotNull CommandSourceStack source, @NotNull Collection<Entity> targets, String input){
        Collection<DataComponentType<?>> types = new HashSet<>();
        String[] args = input.replace(" ", "").split(",");
        for(String s : args){
            Key key = Crux.key(s);
            DataComponentType<?> type = CruxRegistries.DATA_COMPONENT_TYPE.get(key);
            if(type == null) continue;
            types.add(type);
        }
        int given = mainHandArgument(source, targets, e ->{
            for(DataComponentType<?> type : types){
                e.set(type, null);
            }
        });
        Communicator.chat("Removed components on main hand items from " + given + " entities.").use(getExecutor(source));
        return given > 0 ? 1 : -1;
    }

    public static int clearComponents(@NotNull CommandSourceStack source, @NotNull Collection<Entity> targets){
        int given = mainHandArgument(source, targets, CruxItem::clearComponents);
        Communicator.chat("Cleared components on main hand items from " + given + " entities.").use(getExecutor(source));
        return given > 0 ? 1 : -1;
    }

    public static int applyComponents(@NotNull CommandSourceStack source, @NotNull Collection<Entity> targets, @NotNull String input){
        DynamicItemCruxComponents c = new DynamicItemCruxComponents(input);
        int given = mainHandArgument(source, targets, item ->{
            c.apply(item, TextParserContext.empty());
        });
        Communicator.chat("Applied components on main hand items from " + given + " entities.").use(getExecutor(source));
        return given > 0 ? 1 : -1;
    }

    public static int itemName(@NotNull CommandSourceStack source, @NotNull Collection<Entity> targets, @Nullable Component name){
        int given = mainHandArgument(source, targets, item ->{
            item.itemName(name);
        });
        Communicator.chat("Changed the item name of main hand items on " + given + " entities.").use(getExecutor(source));
        return given > 0 ? 1 : -1;
    }

    public static int customName(@NotNull CommandSourceStack source, @NotNull Collection<Entity> targets, @Nullable Component name){
        int given = mainHandArgument(source, targets, item ->{
            item.customName(name);
        });
        Communicator.chat("Changed the custom name of main hand items on " + given + " entities.").use(getExecutor(source));
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
        Communicator.chat("Gave " + item.key() + " to " + CruxMath.format(given) + " entities.").use(getExecutor(source));
        return result;
    }
}
