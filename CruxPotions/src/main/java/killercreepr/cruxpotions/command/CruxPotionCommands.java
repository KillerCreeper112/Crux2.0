package killercreepr.cruxpotions.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import killercreepr.crux.Crux;
import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.util.CruxItem;
import killercreepr.cruxpotions.command.argument.CruxPotionArguments;
import killercreepr.cruxpotions.command.argument.resolver.BlockInflictorResolver;
import killercreepr.cruxpotions.command.argument.resolver.EntityInflictorResolver;
import killercreepr.cruxpotions.data.PotionHolder;
import killercreepr.cruxpotions.persistence.PotionPersistTags;
import killercreepr.cruxpotions.persistence.StoredPotion;
import killercreepr.cruxpotions.persistence.StoredPotionImpl;
import killercreepr.cruxpotions.potions.ActivePotion;
import killercreepr.cruxpotions.potions.CruxPotion;
import killercreepr.cruxpotions.potions.inflictor.BlockInflictor;
import killercreepr.cruxpotions.potions.inflictor.EntityInflictor;
import killercreepr.cruxpotions.potions.inflictor.PotionInflictor;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class CruxPotionCommands {
    public static void register(@NotNull CruxPlugin plugin){
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event ->{
            final Commands commands = event.registrar();
            LiteralCommandNode<CommandSourceStack> cmd = build(Commands.literal("cruxpotion")
                .requires(source -> source.getSender().hasPermission("cruxpotions.cmds.cruxpotion.use")),
                plugin.getLifecycleManager());
            commands.register(cmd, List.of("cpotion", "cpot"));
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
                            Commands.argument("potion", CruxPotionArguments.cruxPotion())
                                .executes(ctx -> applyEffect(
                                    ctx.getSource(),
                                    ctx.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                    ctx.getArgument("potion", CruxPotion.class), 600, 0
                                ))
                                .then(
                                    Commands.argument("duration", ArgumentTypes.time(1))
                                        .executes(ctx -> applyEffect(
                                            ctx.getSource(),
                                            ctx.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                            ctx.getArgument("potion", CruxPotion.class),
                                            ctx.getArgument("duration", Integer.class), 0
                                        ))
                                        .then(
                                            buildDurationAfter(null)
                                        )
                                )
                                .then(
                                    Commands.literal("infinite")
                                        .executes(ctx -> applyEffect(
                                            ctx.getSource(),
                                            ctx.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                            ctx.getArgument("potion", CruxPotion.class),
                                            -1, 0
                                        ))
                                        .then(
                                            buildDurationAfter(-1)
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
                        Commands.argument("potion", CruxPotionArguments.cruxPotion())
                            .executes(ctx -> removeEffect(
                                ctx.getSource(),
                                ctx.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                ctx.getArgument("potion", CruxPotion.class)
                            ))
                    )
            )
        ).then(
            Commands.literal("item").then(
                Commands.literal("give").then(
                    Commands.argument("entity", ArgumentTypes.entities())
                        .then(
                            Commands.argument("potion", CruxPotionArguments.cruxPotion())
                                .executes(ctx -> applyEffectItem(
                                    ctx.getSource(),
                                    ctx.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                    ctx.getArgument("potion", CruxPotion.class), 600, 0
                                ))
                                .then(
                                    Commands.argument("duration", ArgumentTypes.time(1))
                                        .executes(ctx -> applyEffectItem(
                                            ctx.getSource(),
                                            ctx.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                            ctx.getArgument("potion", CruxPotion.class),
                                            ctx.getArgument("duration", Integer.class), 0
                                        ))
                                        .then(
                                            buildDurationAfterItem(null)
                                        )
                                )
                                .then(
                                    Commands.literal("infinite")
                                        .executes(ctx -> applyEffectItem(
                                            ctx.getSource(),
                                            ctx.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                            ctx.getArgument("potion", CruxPotion.class),
                                            -1, 0
                                        ))
                                        .then(
                                            buildDurationAfterItem(-1)
                                        )
                                )
                        )
                )
            ).then(
                Commands.literal("clear").then(
                    Commands.argument("entity", ArgumentTypes.entities())
                        .executes(ctx -> clearEffectsItem(
                            ctx.getSource(),
                            ctx.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(ctx.getSource())
                        ))
                        .then(
                            Commands.argument("potion", CruxPotionArguments.cruxPotion())
                                .executes(ctx -> removeEffectItem(
                                    ctx.getSource(),
                                    ctx.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                    ctx.getArgument("potion", CruxPotion.class)
                                ))
                        )
                )
            )
        )
        ;
        return dispatcher.build();
    }



    public static RequiredArgumentBuilder<CommandSourceStack, ?> buildDurationAfter(@Nullable Integer duration){
        return Commands.argument("amplifier", IntegerArgumentType.integer())
            .executes(ctx -> applyEffect(
                ctx.getSource(),
                ctx.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                ctx.getArgument("potion", CruxPotion.class),
                duration == null ? ctx.getArgument("duration", Integer.class) : duration,
                ctx.getArgument("amplifier", Integer.class)
            ))
            .then(
                Commands.argument("entity_inflictor", CruxPotionArguments.entityInflictor())
                    .executes(ctx -> applyEffect(
                        ctx.getSource(),
                        ctx.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                        ctx.getArgument("potion", CruxPotion.class),
                        duration == null ? ctx.getArgument("duration", Integer.class) : duration,
                        ctx.getArgument("amplifier", Integer.class),
                        ctx.getArgument("entity_inflictor", EntityInflictorResolver.class).resolve(ctx.getSource())
                    ))
            )
            .then(
                Commands.argument("block_inflictor", CruxPotionArguments.blockInflictor())
                    .executes(ctx -> applyEffect(
                        ctx.getSource(),
                        ctx.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                        ctx.getArgument("potion", CruxPotion.class),
                        duration == null ? ctx.getArgument("duration", Integer.class) : duration,
                        ctx.getArgument("amplifier", Integer.class),
                        ctx.getArgument("block_inflictor", BlockInflictorResolver.class).resolve(ctx.getSource())
                    ))
            );
    }

    public static int applyEffectItem(@NotNull CommandSourceStack source, @NotNull Collection<Entity> to,
                                  @NotNull CruxPotion potion,
                                  int duration, int amplifier){
        int effected = 0;
        StoredPotion stored = new StoredPotionImpl(potion, duration, amplifier);
        for(Entity e : to){
            if(!(e instanceof LivingEntity le) || le.getEquipment() == null) continue;
            ItemStack item = le.getEquipment().getItemInMainHand();
            if(CruxItem.isEmpty(item)) continue;

            Collection<StoredPotion> storedPotions = PotionPersistTags.STORED_CUSTOM_POTIONS.get(item, new HashSet<>());
            storedPotions.add(stored);
            PotionPersistTags.STORED_CUSTOM_POTIONS.set(item, storedPotions);
            Crux.itemUpdater().update(item, e);
            if(!(e instanceof Player)){
                le.getEquipment().setItemInMainHand(item, true);
            }

            effected++;
        }
        int value = effected < 1 ? -1 : 1;
        String durationFormat = duration == -1 ? "infinite" : duration+"";
        getExecutor(source).sendMessage("Applied crux potion, " + potion.getName() + ", to " + effected + " the main hand item of entities for " + durationFormat + " ticks with an amplifier of " + amplifier + ".");
        return value;
    }

    public static RequiredArgumentBuilder<CommandSourceStack, ?> buildDurationAfterItem(@Nullable Integer duration){
        return Commands.argument("amplifier", IntegerArgumentType.integer())
            .executes(ctx -> applyEffect(
                ctx.getSource(),
                ctx.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                ctx.getArgument("potion", CruxPotion.class),
                duration == null ? ctx.getArgument("duration", Integer.class) : duration,
                ctx.getArgument("amplifier", Integer.class)
            ));
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
        return applyEffect(source, to, potion, duration, amplifier, parseInflictor(source));
    }

    public static int applyEffect(@NotNull CommandSourceStack source, @NotNull Collection<Entity> to,
                                  @NotNull CruxPotion potion,
                                  int duration, int amplifier, @Nullable PotionInflictor inflictor){
        int effected = 0;
        for(Entity e : to){
            PotionHolder holder = EntityMemory.getOrCreateDataHolder(e, PotionHolder.class);
            if(holder == null) continue;

            ActivePotion pot = potion.create(e, duration, amplifier, inflictor);
            if(!holder.addPotion(pot).isCancelled()) effected++;
        }
        int value = effected < 1 ? -1 : 1;
        String durationFormat = duration == -1 ? "infinite" : duration+"";
        getExecutor(source).sendMessage("Applied crux potion, " + potion.getName() + ", to " + effected + " entities for " + durationFormat + " ticks with an amplifier of " + amplifier + ".");
        return value;
    }

    public static int removeEffect(@NotNull CommandSourceStack source, @NotNull Collection<Entity> from,
                                  @NotNull CruxPotion potion){
        int effected = 0;
        for(Entity e : from){
            PotionHolder holder = EntityMemory.getOrCreateDataHolder(e, PotionHolder.class);
            if(holder == null) continue;

            if(holder.removePotionCheck(potion)) effected++;
        }
        int value = effected < 1 ? -1 : 1;
        getExecutor(source).sendMessage("Removed " + potion.getName() + " from " + effected + " entities.");
        return value;
    }

    public static int clearEffects(@NotNull CommandSourceStack source, @NotNull Collection<Entity> from){
        int effected = 0;
        for(Entity e : from){
            PotionHolder holder = EntityMemory.getOrCreateDataHolder(e, PotionHolder.class);
            if(holder == null) continue;

            if(holder.clearPotions() > 0) effected++;
        }
        int value = effected < 1 ? -1 : 1;
        getExecutor(source).sendMessage("Cleared all potions from " + effected + " entities.");
        return value;
    }

    public static int clearEffectsItem(@NotNull CommandSourceStack source, @NotNull Collection<Entity> from){
        int effected = 0;
        for(Entity e : from){
            if(!(e instanceof LivingEntity le) || le.getEquipment() == null) continue;
            ItemStack item = le.getEquipment().getItemInMainHand();
            if(CruxItem.isEmpty(item)) continue;

            PotionPersistTags.STORED_CUSTOM_POTIONS.set(item, null);
            Crux.itemUpdater().update(item, e);
            if(!(e instanceof Player)){
                le.getEquipment().setItemInMainHand(item, true);
            }

            effected++;
        }
        int value = effected < 1 ? -1 : 1;
        getExecutor(source).sendMessage("Cleared all potions from " + effected + " main hand items of entities.");
        return value;
    }

    public static int removeEffectItem(@NotNull CommandSourceStack source, @NotNull Collection<Entity> from,
                                   @NotNull CruxPotion potion){
        int effected = 0;
        for(Entity e : from){
            if(!(e instanceof LivingEntity le) || le.getEquipment() == null) continue;
            ItemStack item = le.getEquipment().getItemInMainHand();
            if(CruxItem.isEmpty(item)) continue;

            Collection<StoredPotion> storedPotions = PotionPersistTags.STORED_CUSTOM_POTIONS.get(item, null);
            if(storedPotions==null) continue;
            storedPotions.removeIf(stored -> stored.getPotion().compare(potion));
            PotionPersistTags.STORED_CUSTOM_POTIONS.set(item, storedPotions);
            Crux.itemUpdater().update(item, e);
            if(!(e instanceof Player)){
                le.getEquipment().setItemInMainHand(item, true);
            }

            effected++;
        }
        int value = effected < 1 ? -1 : 1;
        getExecutor(source).sendMessage("Removed " + potion.getName() + " from " + effected + " main hand items of entities.");
        return value;
    }
}
