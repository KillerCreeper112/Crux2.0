package killercreepr.cruxblocks.command;

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
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.command.argument.CruxBlocksArguments;
import killercreepr.cruxblocks.persistence.CruxBlocksPersistTags;
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
        )
        ;
        return dispatcher.build();
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
