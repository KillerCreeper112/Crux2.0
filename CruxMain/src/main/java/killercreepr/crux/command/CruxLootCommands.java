package killercreepr.crux.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import killercreepr.crux.command.argument.CruxCmdArguments;
import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.item.ItemLootTable;
import killercreepr.crux.persistence.CruxPersist;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.util.CruxEntityUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CruxLootCommands {
    public static void register(@NotNull CruxPlugin plugin){
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event ->{
            final Commands commands = event.registrar();
            LiteralCommandNode<CommandSourceStack> cmd = build(Commands.literal("cruxloot")
                .requires(source -> source.getSender().hasPermission("crux.cmds.cruxloot.use")),
                plugin.getLifecycleManager());
            commands.register(cmd, List.of("cloot"));
        });
    }

    public static LiteralCommandNode<CommandSourceStack> build(LiteralArgumentBuilder<CommandSourceStack> dispatcher,
                                                                  LifecycleEventManager<?> manager){
        ///effect give killercreepr minecraft:absorption 1 2 true
        //cruxpotion give <player> <potion> <duration> <amplifier> <hide_particles>
        dispatcher.then(
            Commands.literal("tag")
                .then(
                    Commands.literal("set")
                        .then(
                            Commands.argument("target", ArgumentTypes.player())
                                .then(
                                    Commands.argument("loot_table", CruxCmdArguments.ITEM_LOOT_TABLE)
                                        .executes(ctx ->{
                                            Player p = ctx.getArgument("target", PlayerSelectorArgumentResolver.class)
                                                .resolve(ctx.getSource()).getFirst();
                                            ItemLootTable lootTable = ctx.getArgument("loot_table", ItemLootTable.class);
                                            ItemStack item = p.getInventory().getItemInMainHand();
                                            CruxPersist.ITEM_LOOT_TABLES.set(item, List.of(lootTable));
                                            p.sendMessage("Set item loot table tag on main hand item! " + lootTable.key());
                                            return 1;
                                        })
                                )
                        )
                ).then(
                    Commands.literal("add")
                        .then(
                            Commands.argument("target", ArgumentTypes.player())
                                .then(
                                    Commands.argument("loot_table", CruxCmdArguments.ITEM_LOOT_TABLE)
                                        .executes(ctx ->{
                                            Player p = ctx.getArgument("target", PlayerSelectorArgumentResolver.class)
                                                .resolve(ctx.getSource()).getFirst();
                                            ItemLootTable lootTable = ctx.getArgument("loot_table", ItemLootTable.class);
                                            ItemStack item = p.getInventory().getItemInMainHand();
                                            List<ItemLootTable> list = new ArrayList<>(CruxPersist.ITEM_LOOT_TABLES.get(item, List.of()));
                                            list.add(lootTable);
                                            CruxPersist.ITEM_LOOT_TABLES.set(item, list);
                                            p.sendMessage("Added item loot table tag on main hand item! " + lootTable.key());
                                            return 1;
                                        })
                                )
                        )
                ).then(
                    Commands.literal("remove")
                        .then(
                            Commands.argument("target", ArgumentTypes.player())
                                .then(
                                    Commands.argument("loot_table", CruxCmdArguments.ITEM_LOOT_TABLE)
                                        .executes(ctx ->{
                                            Player p = ctx.getArgument("target", PlayerSelectorArgumentResolver.class)
                                                .resolve(ctx.getSource()).getFirst();
                                            ItemLootTable lootTable = ctx.getArgument("loot_table", ItemLootTable.class);
                                            ItemStack item = p.getInventory().getItemInMainHand();
                                            List<ItemLootTable> list = new ArrayList<>(CruxPersist.ITEM_LOOT_TABLES.get(item, List.of()));
                                            list.remove(lootTable);
                                            CruxPersist.ITEM_LOOT_TABLES.set(item, list);
                                            p.sendMessage("Removed item loot table tag on main hand item! " + lootTable.key());
                                            return 1;
                                        })
                                )
                        )
                ).then(
                    Commands.literal("clear")
                        .then(
                            Commands.argument("target", ArgumentTypes.player())
                                .executes(ctx ->{
                                    Player p = ctx.getArgument("target", PlayerSelectorArgumentResolver.class)
                                        .resolve(ctx.getSource()).getFirst();
                                    ItemStack item = p.getInventory().getItemInMainHand();
                                    CruxPersist.ITEM_LOOT_TABLES.set(item, null);
                                    p.sendMessage("Cleared item loot table tag on main hand item!");
                                    return 1;
                                })
                        )
                )
        ).then(
            Commands.literal("give")
                .then(
                    Commands.argument("targets", ArgumentTypes.players())
                        .then(
                            Commands.argument("loot_table", CruxCmdArguments.ITEM_LOOT_TABLE)
                                .executes(ctx ->{
                                    Collection<Player> players = ctx.getArgument("targets", PlayerSelectorArgumentResolver.class)
                                        .resolve(ctx.getSource());
                                    ItemLootTable lootTable = ctx.getArgument("loot_table", ItemLootTable.class);

                                    for(Player p : players){
                                        LootContext.Builder builder = LootContext.builder()
                                            .looter(p);
                                        CruxEntityUtil.giveOrDrop(p, lootTable.populateLoot(builder.build()));
                                    }
                                    getExecutor(ctx.getSource())
                                        .sendMessage("Generated and gave " + players.size() + " players loot from " + lootTable.key() + ".");
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
