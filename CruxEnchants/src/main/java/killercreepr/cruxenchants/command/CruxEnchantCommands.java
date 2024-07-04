package killercreepr.cruxenchants.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import killercreepr.crux.Crux;
import killercreepr.crux.data.communication.MsgContainer;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.util.CruxItem;
import killercreepr.cruxenchants.command.argument.CruxEnchantArguments;
import killercreepr.cruxenchants.enchant.CruxEnchant;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CruxEnchantCommands {
    public static void register(@NotNull CruxPlugin plugin){
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event ->{
            final Commands commands = event.registrar();
            LiteralCommandNode<CommandSourceStack> cmd = build(Commands.literal("cruxenchant")
                .requires(source -> source.getSender().hasPermission("cruxenchants.cmds.cruxenchant.use")),
                plugin.getLifecycleManager());
            commands.register(cmd, List.of("cenchant", "cruxenchants"));
        });
    }

    public static LiteralCommandNode<CommandSourceStack> build(LiteralArgumentBuilder<CommandSourceStack> dispatcher,
                                                                  LifecycleEventManager<?> manager){
        dispatcher.then(
            Commands.argument("targets", ArgumentTypes.entities())
                .then(
                    Commands.literal("set")
                        .then(
                            Commands.argument("enchant", CruxEnchantArguments.cruxEnchant())
                                .executes(ctx -> setEnchant(
                                    ctx.getSource(),
                                    ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                    ctx.getArgument("enchant", CruxEnchant.class),
                                    1, null
                                ))
                                .then(
                                    Commands.argument("level", IntegerArgumentType.integer())
                                        .executes(ctx -> setEnchant(
                                            ctx.getSource(),
                                            ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                            ctx.getArgument("enchant", CruxEnchant.class),
                                            ctx.getArgument("level", Integer.class),
                                            null
                                        ))
                                        .then(
                                            Commands.argument("store", StringArgumentType.string())
                                                .suggests((context, builder) -> builder.suggest("true").suggest("false").buildFuture())
                                                .executes(ctx -> setEnchant(
                                                    ctx.getSource(),
                                                    ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                                    ctx.getArgument("enchant", CruxEnchant.class),
                                                    ctx.getArgument("level", Integer.class),
                                                    ctx.getArgument("store", String.class).equalsIgnoreCase("true")
                                                ))
                                        )
                                )
                        )
                )
                .then(
                    Commands.literal("remove")
                        .then(
                            Commands.argument("enchant", CruxEnchantArguments.cruxEnchant())
                                .executes(ctx -> removeEnchant(
                                    ctx.getSource(),
                                    ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                    ctx.getArgument("enchant", CruxEnchant.class), null
                                ))
                                .then(
                                    Commands.argument("store", StringArgumentType.string())
                                        .suggests((context, builder) -> builder.suggest("true").suggest("false").buildFuture())
                                        .executes(ctx -> removeEnchant(
                                            ctx.getSource(),
                                            ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                            ctx.getArgument("enchant", CruxEnchant.class),
                                            ctx.getArgument("store", String.class).equalsIgnoreCase("true")
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

    public static int removeEnchant(@NotNull CommandSourceStack source, @NotNull Collection<Entity> list, @NotNull CruxEnchant enchant, Boolean store){
        int changed = 0;
        for(Entity e : list){
            if(!(e instanceof LivingEntity le) || le.getEquipment() == null) return -1;
            ItemStack item = le.getEquipment().getItemInMainHand();
            if(CruxItem.isEmpty(item)) return -1;
            int x = removeEnchant(item, enchant, store);
            Crux.handlers().item().update(item, e);
            if(x > 0) changed++;
            if(!(e instanceof Player)) le.getEquipment().setItemInMainHand(item);
        }
        new MsgContainer("Removed enchant, " + enchant.getName() + " on " + changed + " entities.")
            .use(getExecutor(source));
        return changed > 0 ? 1 : -1;
    }

    public static int setEnchant(@NotNull CommandSourceStack source, @NotNull Collection<Entity> list, @NotNull CruxEnchant enchant, int level, Boolean store){
        int changed = 0;
        for(Entity e : list){
            if(!(e instanceof LivingEntity le) || le.getEquipment() == null) return -1;
            ItemStack item = le.getEquipment().getItemInMainHand();
            if(CruxItem.isEmpty(item)) return -1;
            int x = setEnchant(item, enchant, level, store);
            Crux.handlers().item().update(item, e);
            if(x > 0) changed++;
            if(!(e instanceof Player)) le.getEquipment().setItemInMainHand(item);
        }
        new MsgContainer("Set enchant, " + enchant.getName() + " to level " + level + " on " + changed + " entities.")
            .use(getExecutor(source));
        return changed > 0 ? 1 : -1;
    }

    public static int setEnchant(@NotNull ItemStack item, @NotNull CruxEnchant enchant, int level, Boolean store){
        if(store == null){
            store = item.getItemMeta() instanceof EnchantmentStorageMeta;
        }
        if(store){
            CruxEnchant.setStored(item, enchant, level, true);
        }else CruxEnchant.set(item, enchant, level, true);
        return 1;
    }

    public static int removeEnchant(@NotNull ItemStack item, @NotNull CruxEnchant enchant, Boolean store){
        if(store == null){
            store = item.getItemMeta() instanceof EnchantmentStorageMeta;
        }
        if(store){
            CruxEnchant.removeStored(item, enchant);
        }else CruxEnchant.remove(item, enchant);
        return 1;
    }
}
