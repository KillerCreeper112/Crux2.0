package killercreepr.cruxblocks.core.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import killercreepr.crux.core.communication.MsgContainer;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.cruxblocks.api.block.CruxBlock;
import killercreepr.cruxblocks.api.block.context.BlockContext;
import killercreepr.cruxblocks.api.block.context.PlaceBlockContext;
import killercreepr.cruxblocks.api.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.core.command.argument.CruxBlocksArguments;
import killercreepr.cruxblocks.core.persistence.CruxBlocksPersistTags;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.BlockCommandSender;
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

public class CruxBlocksCommands {
    public static void register(@NotNull CruxPlugin plugin){
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event ->{
            final Commands commands = event.registrar();
            LiteralCommandNode<CommandSourceStack> cmd = build(Commands.literal("cruxblocks")
                .requires(source -> source.getSender().hasPermission("cruxblocks.cmds.cruxblocks.use")), plugin.getLifecycleManager());
            commands.register(cmd, List.of("cb", "cruxblock", "cblock"));
        });
    }

    public static LiteralCommandNode<CommandSourceStack> build(LiteralArgumentBuilder<CommandSourceStack> dispatcher,
                                                               LifecycleEventManager<?> manager){
        //give <player> <id> <amount>
        dispatcher.then(
            Commands.literal("tag")
                .then(
                    Commands.argument("targets", ArgumentTypes.entities())
                        .then(
                            Commands.argument("block", CruxBlocksArguments.cruxBlockGroup())
                                .executes(ctx -> tag(
                                    ctx.getSource(),
                                    ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                    ctx.getArgument("block", CruxBlockGroup.class)
                                ))
                        )
                )
        ).then(
            Commands.literal("place")
                .then(
                    Commands.argument("location", ArgumentTypes.blockPosition())
                        .then(
                            Commands.argument("block", CruxBlocksArguments.cruxBlockGroup())
                                .executes(ctx ->{
                                    CommandSender sender = getExecutor(ctx.getSource());
                                    World world = getWorld(ctx.getSource());
                                    if(world == null) return -1;
                                    BlockPosition pos = ctx.getArgument("location", BlockPositionResolver.class)
                                        .resolve(ctx.getSource());
                                    CruxBlockGroup group = ctx.getArgument("block", CruxBlockGroup.class);
                                    Block block = pos.toLocation(world).getBlock();
                                    group.placeBlock(PlaceBlockContext.context(block, null, BlockFace.DOWN));
                                    sender.sendMessage("Placed group, " + group.key() + ", " + pos.blockX() + ", " + pos.blockY() + ", " + pos.blockZ() + ".");
                                    return 1;
                                })
                        )
                )
        ).then(
            Commands.literal("set")
                .then(
                    Commands.argument("location", ArgumentTypes.blockPosition())
                        .then(
                            Commands.argument("block", CruxBlocksArguments.cruxBlock())
                                .executes(ctx ->{
                                    CommandSender sender = getExecutor(ctx.getSource());
                                    World world = getWorld(ctx.getSource());
                                    if(world == null) return -1;
                                    BlockPosition pos = ctx.getArgument("location", BlockPositionResolver.class)
                                        .resolve(ctx.getSource());
                                    CruxBlock group = ctx.getArgument("block", CruxBlock.class);
                                    Block block = pos.toLocation(world).getBlock();
                                    group.setBlock(BlockContext.context(block, null), true);
                                    sender.sendMessage("Set crux block, " + group.key() + ", " + pos.blockX() + ", " + pos.blockY() + ", " + pos.blockZ() + ".");
                                    return 1;
                                })
                        )
                )
        ).then(
            Commands.literal("placeblock")
                .then(
                    Commands.argument("location", ArgumentTypes.blockPosition())
                        .then(
                            Commands.argument("block", CruxBlocksArguments.cruxBlock())
                                .executes(ctx ->{
                                    CommandSender sender = getExecutor(ctx.getSource());
                                    World world = getWorld(ctx.getSource());
                                    if(world == null) return -1;
                                    BlockPosition pos = ctx.getArgument("location", BlockPositionResolver.class)
                                        .resolve(ctx.getSource());
                                    CruxBlock group = ctx.getArgument("block", CruxBlock.class);
                                    Block block = pos.toLocation(world).getBlock();
                                    group.placeBlock(PlaceBlockContext.context(block, null, BlockFace.DOWN));
                                    sender.sendMessage("Placed crux block, " + group.key() + ", at " + pos.blockX() + ", " + pos.blockY() + ", " + pos.blockZ() + ".");
                                    return 1;
                                })
                        )
                )
        )
        ;
        return dispatcher.build();
    }

    public static World getWorld(@NotNull CommandSourceStack source){
        CommandSender sender = getExecutor(source);
        if(sender instanceof BlockCommandSender s) return s.getBlock().getWorld();
        if(sender instanceof Entity s) return s.getWorld();
        return null;
    }

    public static @NotNull CommandSender getExecutor(@NotNull CommandSourceStack source){
        return Objects.requireNonNullElse(source.getExecutor(), source.getSender());
    }

    public static int tag(@NotNull CommandSourceStack source, @NotNull Collection<Entity> targets, @NotNull CruxBlockGroup group){
        int given = 0;
        for(Entity entity : targets){
            if(!(entity instanceof LivingEntity e)) continue;

            EntityEquipment equip = e.getEquipment();
            if(equip == null) continue;

            ItemStack item = equip.getItemInMainHand();
            CruxBlocksPersistTags.CRUX_BLOCK_GROUP.set(item, group);

            given++;
            if(e instanceof Player p) continue;
            equip.setItemInMainHand(item, true);
        }
        int result = given > 0 ? 1 : -1;
        new MsgContainer("<yellow>Set tag " + group.key() + " to " + CruxMath.format(given) + " entities.").use(getExecutor(source));
        return result;
    }
}
