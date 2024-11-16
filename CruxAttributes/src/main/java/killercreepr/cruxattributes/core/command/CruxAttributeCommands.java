package killercreepr.cruxattributes.core.command;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
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
import killercreepr.crux.tags.container.TagContainer;
import killercreepr.crux.tags.resolver.Tag;
import killercreepr.crux.util.CruxItem;
import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.api.attribute.CruxSlot;
import killercreepr.cruxattributes.core.attribute.CruxAttributeModifier;
import killercreepr.cruxattributes.core.command.argument.CruxAttributeArguments;
import killercreepr.cruxattributes.core.registries.CruxAttributeRegistries;
import net.kyori.adventure.key.Key;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class CruxAttributeCommands {
    public static void register(@NotNull CruxPlugin plugin){
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event ->{
            final Commands commands = event.registrar();
            LiteralCommandNode<CommandSourceStack> cmd = build(Commands.literal("cruxattribute")
                .requires(source -> source.getSender().hasPermission("cruxattributes.cmds.cruxattribute.use")),
                plugin.getLifecycleManager());
            commands.register(cmd, List.of("cattribute", "catt"));
        });
    }

    public static LiteralCommandNode<CommandSourceStack> build(LiteralArgumentBuilder<CommandSourceStack> dispatcher,
                                                                  LifecycleEventManager<?> manager){
        ///effect give killercreepr minecraft:absorption 1 2 true
        //cruxpotion give <player> <potion> <duration> <amplifier> <hide_particles>
        dispatcher.then(
            Commands.literal("add")
                .then(
                    Commands.argument("targets", ArgumentTypes.entities())
                        .then(
                            Commands.argument("execute_operation", StringArgumentType.string())
                                .suggests((context, builder) -> builder.suggest("self").suggest("hand").buildFuture())
                                .then(
                                    Commands.argument("attribute", CruxAttributeArguments.cruxAttribute())
                                        .then(
                                            Commands.argument("modkey", CruxCmdArguments.CRUX_KEY)
                                                .then(
                                                    Commands.argument("amount", DoubleArgumentType.doubleArg())
                                                        .executes(ctx -> addModifier(
                                                            ctx.getSource(),
                                                            ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                                            ctx.getArgument("execute_operation", String.class),
                                                            ctx.getArgument("attribute", CruxAttribute.class),
                                                            new CruxAttributeModifier(
                                                                ctx.getArgument("modkey", Key.class),
                                                                ctx.getArgument("amount", Double.class)
                                                            )
                                                        ))
                                                        .then(
                                                            Commands.argument("slot", CruxAttributeArguments.cruxSlot())
                                                                .executes(ctx -> addModifier(
                                                                    ctx.getSource(),
                                                                    ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                                                    ctx.getArgument("execute_operation", String.class),
                                                                    ctx.getArgument("attribute", CruxAttribute.class),
                                                                    new CruxAttributeModifier(
                                                                        ctx.getArgument("modkey", Key.class),
                                                                        ctx.getArgument("amount", Double.class),
                                                                        ctx.getArgument("slot", CruxSlot.class)
                                                                    )
                                                                ))
                                                                .then(
                                                                    Commands.argument("operation", CruxAttributeArguments.cruxAttributeOperation())
                                                                        .executes(ctx -> addModifier(
                                                                            ctx.getSource(),
                                                                            ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                                                            ctx.getArgument("execute_operation", String.class),
                                                                            ctx.getArgument("attribute", CruxAttribute.class),
                                                                            new CruxAttributeModifier(
                                                                                ctx.getArgument("modkey", Key.class),
                                                                                ctx.getArgument("amount", Double.class),
                                                                                ctx.getArgument("operation", CruxAttribute.Operation.class),
                                                                                ctx.getArgument("slot", CruxSlot.class)
                                                                            )
                                                                        ))
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        ).then(
            Commands.literal("remove")
                .then(
                    Commands.argument("targets", ArgumentTypes.entities())
                            .then(
                                Commands.argument("execute_operation", StringArgumentType.string())
                                    .suggests((context, builder) -> builder.suggest("self").suggest("hand").buildFuture())
                                    .then(
                                        Commands.argument("attribute", CruxAttributeArguments.cruxAttribute())
                                            .then(
                                                Commands.argument("modkey", CruxCmdArguments.CRUX_KEY)
                                                    .executes(ctx -> removeModifier(
                                                        ctx.getSource(),
                                                        ctx.getArgument("targets", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()),
                                                        ctx.getArgument("execute_operation", String.class),
                                                        ctx.getArgument("attribute", CruxAttribute.class),
                                                        ctx.getArgument("modkey", Key.class)
                                                    ))
                                            )
                                    )
                            )
                )
        ).then(
            Commands.literal("list").then(
                Commands.argument("target", ArgumentTypes.entity())
                    .then(
                        Commands.argument("execute_operation", StringArgumentType.string())
                            .suggests((context, builder) -> builder.suggest("self").suggest("hand").buildFuture())
                            .executes(ctx -> listModifier(
                                ctx.getSource(),
                                ctx.getArgument("target", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst(),
                                ctx.getArgument("execute_operation", String.class)
                            ))
                            .then(
                                Commands.argument("attribute", CruxAttributeArguments.cruxAttribute())
                                    .executes(ctx -> listModifier(
                                        ctx.getSource(),
                                        ctx.getArgument("target", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()).getFirst(),
                                        ctx.getArgument("execute_operation", String.class),
                                        ctx.getArgument("attribute", CruxAttribute.class)
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

    public static int listModifier(
        @NotNull CommandSourceStack source,
        @NotNull Entity from,
        @NotNull String executeOperation
    ){
        int highest = 0;
        for(CruxAttribute a : CruxAttributeRegistries.ATTRIBUTES){
            int x = listModifier(source, from, executeOperation, a);
            if(x > highest) highest = x;
            if(x==-1){
                new MsgContainer("<red>There are no modifiers from " + a.getName() + ".").use(getExecutor(source));
            }
        }
        return highest;
    }

    public static int listModifier(
        @NotNull CommandSourceStack source,
        @NotNull Entity from,
        @NotNull String executeOperation,
        @NotNull CruxAttribute attribute
    ){
        if(!executeOperation.equalsIgnoreCase("hand")) return listModifier(source, from, attribute);
        if(!(from instanceof LivingEntity e) || e.getEquipment() == null) return -1;
        ItemStack item = e.getEquipment().getItemInMainHand();
        if(CruxItem.isEmpty(item)) return -1;
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return -1;
        return listModifier(source, meta, attribute);
    }

    public static <T extends PersistentDataHolder> int listModifier(
        @NotNull CommandSourceStack source,
        @NotNull T from,
        @NotNull CruxAttribute attribute
    ){
        CommandSender sender = getExecutor(source);
        Collection<CruxAttributeModifier> list = CruxAttribute.getModifiers(from, attribute);
        if(list.isEmpty()){
            return -1;
        }else{
            new MsgContainer("<yellow>" + attribute.getName() + ":").use(sender);
            for(CruxAttributeModifier m : list){
                if(m.getPath() != null){
                    int i = 0;
                    new MsgContainer("<gray>PATH:").use(sender);
                    for(Key k : m.getPath()){
                        i++;
                        new MsgContainer(" ".repeat(i) + " -> " + k.asString()).use(sender);
                    }
                }

                String format = "<gold><key> <dark_gray>-> <red>{amount=<amount>, slot=<slot>, operation=<operation>}";
                new MsgContainer(format).use(sender,
                    TagContainer.merged(
                        Tag.parsed("key", m.key().asString()),
                        Tag.parsed("amount", m.getAmount()+""),
                        Tag.parsed("operation", m.getOperation().name().toLowerCase()),
                        Tag.parsed("slot", m.getSlot() == null ? "all" : m.getSlot().toString().toLowerCase())
                    )
                );
            }
        }
        return 1;
    }

    public static <T extends PersistentDataHolder> int removeModifier(
        @NotNull CommandSourceStack source,
        @NotNull Collection<T> to,
        @NotNull CruxAttribute attribute,
        @NotNull Key key
    ){
        for(PersistentDataHolder holder : to){
            CruxAttribute.removeModifier(
                holder, attribute, key
            );
        }
        new MsgContainer("Removed attribute modifier of " + key + ".")
            .use(getExecutor(source));
        return 1;
    }


    public static int removeModifier(
        @NotNull CommandSourceStack source,
        @NotNull Collection<Entity> to,
        @NotNull String executeOperation,
        @NotNull CruxAttribute attribute,
        @NotNull Key key
    ){
        if(!executeOperation.equalsIgnoreCase("hand")) return removeModifier(source, to, attribute, key);
        modifierOperation(to, edit -> removeModifier(source, edit, attribute, key));
        return 1;
    }

    public static <T extends PersistentDataHolder> int addModifier(
        @NotNull CommandSourceStack source,
        @NotNull Collection<T> to,
        @NotNull CruxAttribute attribute,
        @NotNull CruxAttributeModifier modifier
    ){

        for(PersistentDataHolder holder : to){
            CruxAttribute.addModifier(
                holder, attribute, modifier
            );
        }
        new MsgContainer("Added attribute modifier of " + modifier.key() + " with value " + modifier.getAmount() + " and operation " +
            modifier.getOperation().toString().toLowerCase() + " with slot " + modifier.getSlot() + ".")
            .use(getExecutor(source));
        return 1;
    }

    public static void modifierOperation(@NotNull Collection<Entity> to,
                                         @NotNull Consumer<Collection<ItemMeta>> consumer){
        Map<LivingEntity, Map.Entry<ItemStack, ItemMeta>> items = new HashMap<>();
        Collection<ItemMeta> edit = new HashSet<>();
        for(Entity e : to){
            if(!(e instanceof LivingEntity ee)) continue;
            if(ee.getEquipment() == null) continue;
            ItemStack item = ee.getEquipment().getItemInMainHand();
            if(CruxItem.isEmpty(item)) continue;

            ItemMeta meta = item.getItemMeta();
            if(meta==null) continue;
            items.put(ee, Map.entry(item, meta));
            edit.add(meta);
        }
        consumer.accept(edit);
        items.forEach((e, entry) ->{
            ItemStack item = entry.getKey();
            item.setItemMeta(entry.getValue());
            if(e instanceof Player) return;
            e.getEquipment().setItemInMainHand(item, true);
        });
    }

    public static int addModifier(
        @NotNull CommandSourceStack source,
        @NotNull Collection<Entity> to,
        @NotNull String executeOperation,
        @NotNull CruxAttribute attribute,
        @NotNull CruxAttributeModifier modifier
    ){
        if(!executeOperation.equalsIgnoreCase("hand")) return addModifier(source, to, attribute, modifier);
        modifierOperation(to, edit -> addModifier(source, edit, attribute, modifier));
        return 1;
    }

}
