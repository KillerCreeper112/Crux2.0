package killercreepr.cruxpotion.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.range.IntegerRangeProvider;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.cruxpotion.CruxPotionModule;
import killercreepr.cruxpotion.command.args.CruxPotionArgument;
import killercreepr.cruxpotion.command.suggestions.CruxPotionSuggester;
import killercreepr.cruxpotion.data.PotionHolder;
import killercreepr.cruxpotion.event.EntityCruxPotionEvent;
import killercreepr.cruxpotion.potions.ActivePotion;
import killercreepr.cruxpotion.potions.CruxPotion;
import killercreepr.cruxpotion.potions.inflictor.BlockInflictor;
import killercreepr.cruxpotion.potions.inflictor.EntityInflictor;
import killercreepr.cruxpotion.potions.inflictor.PotionInflictor;
import net.minecraft.server.commands.EffectCommands;
import org.apache.logging.log4j.core.LifeCycle;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;

public class CruxPotionCommands {
    public static void register(@NotNull CruxPlugin plugin){
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event ->{
            final Commands commands = event.registrar();
            LiteralCommandNode<CommandSourceStack> cmd = build(Commands.literal("cruxpotion"), plugin.getLifecycleManager());
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
                                                        .suggests(new CruxPotionSuggester())
                                                        .executes(ctx ->{
                                                            CruxPotion potion = ctx.getArgument("potion",
                                                                    CruxPotion.class);
                                                            return applyEffect(
                                                                    ctx.getSource(),
                                                                    ctx.getArgument("entity", EntitySelectorArgumentResolver.class)
                                                                            .resolve(ctx.getSource()),
                                                                    potion, 600, 0
                                                            );
                                                        })
                                                        .then(
                                                                Commands.argument("duration", ArgumentTypes.integerRange())
                                                                        .executes(ctx ->{
                                                                            CruxPotion potion = ctx.getArgument("potion",
                                                                                    CruxPotion.class);
                                                                            int duration = ctx.getArgument("duration",
                                                                                    IntegerRangeProvider.class).range()
                                                                                    .upperEndpoint();
                                                                            return applyEffect(
                                                                                    ctx.getSource(),
                                                                                    ctx.getArgument("entity", EntitySelectorArgumentResolver.class)
                                                                                            .resolve(ctx.getSource()),
                                                                                    potion, duration, 0
                                                                            );
                                                                        })
                                                                        .then(
                                                                                Commands.argument("amplifier", ArgumentTypes.integerRange())
                                                                                        .executes(ctx ->{
                                                                                            CruxPotion potion = ctx.getArgument("potion",
                                                                                                    CruxPotion.class);
                                                                                            int duration = ctx.getArgument("duration",
                                                                                                            IntegerRangeProvider.class).range()
                                                                                                    .upperEndpoint();
                                                                                            int amplifier = ctx.getArgument("amplifier",
                                                                                                            IntegerRangeProvider.class).range()
                                                                                                    .upperEndpoint();
                                                                                            return applyEffect(
                                                                                                    ctx.getSource(),
                                                                                                    ctx.getArgument("entity", EntitySelectorArgumentResolver.class)
                                                                                                            .resolve(ctx.getSource()),
                                                                                                    potion, duration, amplifier
                                                                                            );
                                                                                        })
                                                                                        //.then()
                                                                        )
                                                        )
                                        )
                        )
                        .build()
        );
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
