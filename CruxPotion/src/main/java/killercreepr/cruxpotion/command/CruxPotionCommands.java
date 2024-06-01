package killercreepr.cruxpotion.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.range.IntegerRangeProvider;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.cruxpotion.command.args.CruxPotionArgument;
import killercreepr.cruxpotion.data.PotionHolder;
import killercreepr.cruxpotion.potions.ActivePotion;
import killercreepr.cruxpotion.potions.CruxPotion;
import killercreepr.cruxpotion.potions.inflictor.BlockInflictor;
import killercreepr.cruxpotion.potions.inflictor.EntityInflictor;
import killercreepr.cruxpotion.potions.inflictor.PotionInflictor;
import net.minecraft.server.commands.EffectCommands;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;

public class CruxPotionCommands {
    public static void register(@NotNull CruxPlugin plugin){
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event ->{
            final Commands commands = event.registrar();
            LiteralCommandNode<CommandSourceStack> cmd = build(Commands.literal("ayotest"), plugin.getLifecycleManager());
            commands.register(cmd);
        });
    }

    public static LiteralCommandNode<CommandSourceStack> build(LiteralArgumentBuilder<CommandSourceStack> dispatcher,
                                                                  LifecycleEventManager<?> manager){
        ///effect give killercreepr minecraft:absorption 1 2 true
        //cruxpotion give <player> <potion> <duration> <amplifier> <hide_particles>
        dispatcher.then(
                Commands.literal("give")
                        .then(
                                Commands.argument("entity", ArgumentTypes.entities())
                                        .then(
                                                Commands.argument("potion", CruxPotionArgument.cruxPotion())
                                                        .executes(ctx -> applyEffect(
                                                                ctx.getSource(),
                                                                ctx.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                                                ctx.getArgument("potion", CruxPotion.class), 600, 0
                                                        ))
                                                        .then(
                                                                Commands.argument("duration", IntegerArgumentType.integer(1, Integer.MAX_VALUE))
                                                                        .executes(ctx -> applyEffect(
                                                                                ctx.getSource(),
                                                                                ctx.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                                                                ctx.getArgument("potion", CruxPotion.class),
                                                                                ctx.getArgument("duration", Integer.class), 0
                                                                        ))
                                                                        .then(
                                                                                Commands.argument("amplifier", IntegerArgumentType.integer())
                                                                                        .executes(ctx -> applyEffect(
                                                                                                ctx.getSource(),
                                                                                                ctx.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                                                                                ctx.getArgument("potion", CruxPotion.class),
                                                                                                ctx.getArgument("duration", Integer.class),
                                                                                                ctx.getArgument("amplifier", Integer.class)
                                                                                        ))
                                                                                        //todo sumthin idk .then()
                                                                        )
                                                        )
                                        )
                        )
        ).then(
                Commands.literal("clear").then(
                        Commands.argument("entity", ArgumentTypes.entities())
                                .executes(ctx -> clearEffects(
                                        ctx.getSource(),
                                        ctx.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(ctx.getSource())
                                ))
                                .then(
                                        Commands.argument("potion", CruxPotionArgument.cruxPotion())
                                                .executes(ctx -> removeEffect(
                                                        ctx.getSource(),
                                                        ctx.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                                        ctx.getArgument("potion", CruxPotion.class)
                                                ))
                                )
                )
        )
        ;
        return dispatcher.build();
    }

    public static @Nullable PotionInflictor parseInflictor(@NotNull CommandSourceStack source){
        CommandSender sender = getExecutor(source);
        if(sender instanceof Entity e) return new EntityInflictor(e);
        if(sender instanceof Block b) return new BlockInflictor(b);
        return null;
    }

    public static @NotNull CommandSender getExecutor(@NotNull CommandSourceStack source){
        return Objects.requireNonNullElse(source.getExecutor(), source.getSender());
    }
    public static int applyEffect(@NotNull CommandSourceStack source, @NotNull Collection<Entity> to,
                                  @NotNull CruxPotion potion,
                                  int duration, int amplifier){
        int effected = 0;
        PotionInflictor inflictor = parseInflictor(source);
        for(Entity e : to){
            PotionHolder holder = EntityMemory.getOrCreateDataHolder(e, PotionHolder.class);
            if(holder == null) continue;

            ActivePotion pot = potion.create(e, duration, amplifier, inflictor);
            if(!holder.addPotion(pot).isCancelled()) effected++;
        }
        return effected < 1 ? -1 : 1;
    }

    public static int removeEffect(@NotNull CommandSourceStack source, @NotNull Collection<Entity> from,
                                  @NotNull CruxPotion potion){
        int effected = 0;
        for(Entity e : from){
            PotionHolder holder = EntityMemory.getOrCreateDataHolder(e, PotionHolder.class);
            if(holder == null) continue;

            if(holder.removePotionCheck(potion)) effected++;
        }
        return effected < 1 ? -1 : 1;
    }

    public static int clearEffects(@NotNull CommandSourceStack source, @NotNull Collection<Entity> from){
        int effected = 0;
        for(Entity e : from){
            PotionHolder holder = EntityMemory.getOrCreateDataHolder(e, PotionHolder.class);
            if(holder == null) continue;

            if(holder.clearPotions() > 0) effected++;
        }
        return effected < 1 ? -1 : 1;
    }
}
