package killercreepr.cruxstructures.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import killercreepr.crux.Crux;
import killercreepr.crux.data.communication.MsgContainer;
import killercreepr.crux.data.world.CruxPosition;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.util.CruxMath;
import killercreepr.crux.util.GetNear;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxstructures.commands.argument.StructureArgs;
import killercreepr.cruxstructures.manager.StructureManager;
import killercreepr.cruxstructures.registries.StructureRegistries;
import killercreepr.cruxstructures.structure.Structure;
import killercreepr.cruxstructures.structure.impl.CfgFAWEStructure;
import killercreepr.cruxstructures.structure.stored.StoredStructure;
import killercreepr.cruxstructures.util.GetStructureNear;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StructureCommands {
    protected final @NotNull CruxPlugin plugin;
    protected final @NotNull StructureManager structureManager;
    public StructureCommands(@NotNull CruxPlugin plugin, @NotNull StructureManager structureManager) {
        this.plugin = plugin;
        this.structureManager = structureManager;
    }

    public void register(){
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event ->{
            final Commands commands = event.registrar();
            LiteralCommandNode<CommandSourceStack> cmd = build(Commands.literal("cruxstructures")
                .requires(source -> source.getSender().hasPermission("cruxstructures.cmds.cruxstructures.use")), plugin.getLifecycleManager());
            commands.register(cmd, List.of("cruxstructure", "cstruc"));
        });
    }

    public LiteralCommandNode<CommandSourceStack> build(LiteralArgumentBuilder<CommandSourceStack> dispatcher,
                                                               LifecycleEventManager<?> manager){
        //give <player> <id> <amount>
        dispatcher.then(
            Commands.literal("place")
                .then(
                    Commands.argument("structure", StructureArgs.STRUCTURE)
                        .executes(ctx ->{
                            Structure structure = ctx.getArgument("structure", Structure.class);
                            CommandSender sender = getExecutor(ctx.getSource());

                            Location spawn;
                            if(sender instanceof BlockCommandSender s){
                                spawn = s.getBlock().getLocation();
                            }else if(sender instanceof Entity s){
                                spawn = s.getLocation();
                            }else return -1;

                            structure.place(spawn);
                            sender.sendMessage("Structure, '" + structure.key() + "' has been placed at " +
                                CruxMath.format(spawn.getX()) + ", " + CruxMath.format(spawn.getY()) + ", " + CruxMath.format(spawn.getZ()) + " in world, " +
                                spawn.getWorld().getName() + ".");
                            return 1;
                        })
                        .then(
                            Commands.argument("location", ArgumentTypes.blockPosition())
                                .executes(ctx ->{
                                    Structure structure = ctx.getArgument("structure", Structure.class);
                                    BlockPosition position = ctx.getArgument("location", BlockPositionResolver.class)
                                        .resolve(ctx.getSource());
                                    CommandSender sender = getExecutor(ctx.getSource());

                                    World world;
                                    if(sender instanceof BlockCommandSender s){
                                        world = s.getBlock().getWorld();
                                    }else if(sender instanceof Entity s){
                                        world = s.getWorld();
                                    }else return -1;

                                    Location spawn = new Location(
                                        world, position.x(), position.y(), position.z()
                                    );
                                    structure.place(spawn);
                                    sender.sendMessage("Structure, '" + structure.key() + "' has been placed at " +
                                        CruxMath.format(spawn.getX()) + ", " + CruxMath.format(spawn.getY()) + ", " + CruxMath.format(spawn.getZ()) + " in world, " +
                                        spawn.getWorld().getName() + ".");
                                    return 1;
                                }).then(
                                    Commands.argument("rotation", DoubleArgumentType.doubleArg())
                                        .executes(ctx ->{
                                            Structure structure = ctx.getArgument("structure", Structure.class);
                                            BlockPosition position = ctx.getArgument("location", BlockPositionResolver.class)
                                                .resolve(ctx.getSource());
                                            CommandSender sender = getExecutor(ctx.getSource());

                                            World world;
                                            if(sender instanceof BlockCommandSender s){
                                                world = s.getBlock().getWorld();
                                            }else if(sender instanceof Entity s){
                                                world = s.getWorld();
                                            }else return -1;

                                            Location spawn = new Location(
                                                world, position.x(), position.y(), position.z()
                                            );
                                            structure.place(spawn, ctx.getArgument("rotation", Double.class));
                                            sender.sendMessage("Structure, '" + structure.key() + "' has been placed at " +
                                                CruxMath.format(spawn.getX()) + ", " + CruxMath.format(spawn.getY()) + ", " + CruxMath.format(spawn.getZ()) + " in world, " +
                                                spawn.getWorld().getName() + ".");
                                            return 1;
                                        })
                                )
                        )
                )
        ).then(
            Commands.literal("create")
                .then(
                    Commands.argument("id", StringArgumentType.word())
                        .then(
                            Commands.argument("schematic", StringArgumentType.word())
                                .executes(ctx -> create(
                                    ctx.getSource(), ctx.getArgument("id", String.class),
                                    ctx.getArgument("schematic", String.class),
                                    false, null
                                ))
                                .then(
                                    Commands.argument("persists", BoolArgumentType.bool())
                                        .executes(ctx -> create(
                                            ctx.getSource(), ctx.getArgument("id", String.class),
                                            ctx.getArgument("schematic", String.class),
                                            ctx.getArgument("persists", Boolean.class), null
                                        ))
                                        .then(
                                            Commands.argument("type", StringArgumentType.word())
                                                .executes(ctx -> create(
                                                    ctx.getSource(), ctx.getArgument("id", String.class),
                                                    ctx.getArgument("schematic", String.class),
                                                    ctx.getArgument("persists", Boolean.class),
                                                    ctx.getArgument("type", String.class)
                                                ))
                                        )
                                )
                        )
                )
        ).then(
            Commands.literal("remove")
                .then(
                    Commands.argument("id", StringArgumentType.word())
                        .executes(ctx -> remove(ctx.getSource(), ctx.getArgument("id", String.class)))
                )
        ).then(
            Commands.literal("locate")
                .executes(ctx -> locate(ctx.getSource(), null, null))
                .then(
                    Commands.argument("structure", StructureArgs.STRUCTURE)
                        .executes(ctx -> locate(ctx.getSource(), ctx.getArgument("structure", Structure.class),
                            null))
                        .then(
                            Commands.argument("type", StringArgumentType.word())
                                .suggests((ctx, s) ->{
                                    s.suggest("nearest").suggest("random").suggest("farthest");
                                    return s.buildFuture();
                                })
                                .executes(ctx -> locate(ctx.getSource(), ctx.getArgument("structure", Structure.class),
                                    ctx.getArgument("type", String.class)))
                        )
                )
        ).then(
            Commands.literal("pos")
                .executes(ctx ->{
                    CommandSender sender = getExecutor(ctx.getSource());
                    if(!(sender instanceof Player p)) return -1;
                    Block target = p.getTargetBlockExact(10);

                    StoredStructure stored = structureManager.getFirstStoredAt(StoredStructure.class, target);
                    CruxPosition center = stored.getPosition();

                    CruxPosition targetPos = CruxPosition.block(target);

                    CruxPosition pos = targetPos.subtract(center);
                    sender.sendMessage(Component.text("Relative position: " + pos.blockX() + ", " + pos.blockY() + ", " + pos.blockZ())
                        .clickEvent(ClickEvent.copyToClipboard(pos.blockX() + " " + pos.blockY() + " " + pos.blockZ())));
                    return 1;
                })
        )
        ;
        return dispatcher.build();
    }

    public static @NotNull CommandSender getExecutor(@NotNull CommandSourceStack source){
        return Objects.requireNonNullElse(source.getExecutor(), source.getSender());
    }

    public int locate(@NotNull CommandSourceStack source, @Nullable Structure structure, @Nullable String type){
        if(!(getExecutor(source) instanceof Entity p)) return -1;
        GetNear<StoredStructure> near = new GetStructureNear(structureManager.getStored())
            .center(p.getLocation())
            ;
        if(structure != null){
            near.filter(t -> t.getStructureKey().equals(structure.key()));
        }
        if(type == null) type = "nearest";
        switch (type.toLowerCase()){
            case "nearest" -> near.operation(GetNear.Operation.NEAREST);
            case "farthest" -> near.operation(GetNear.Operation.FARTHEST);
        }

        StoredStructure nearest;
        switch (type.toLowerCase()){
            case "random" ->{
                nearest = CruxMath.getRandom(new ArrayList<>(near.find()));
            }
            default -> nearest = near.findFirst();
        }
        if(nearest==null){
            p.sendMessage("No structures found in " + p.getWorld().getName() + ".");
            return 0;
        }

        CruxPosition pos = nearest.getPosition();

        p.sendMessage(
            Component.text("Structure, " + nearest.getStructureKey() + " found at " + pos.x() + ", " + pos.y() + ", " + pos.z())
                .hoverEvent(HoverEvent.showText(Component.text("Click to teleport to\n" + nearest.getStructureKey())))
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND,
                    "/teleport " + pos.x() + " " + pos.y() + " " + pos.z()
                ))
        );

        return 1;
    }

    public int create(@NotNull CommandSourceStack source, String id, String schematic, boolean persists, String type){
        CommandSender sender = getExecutor(source);
        CruxConfig cfg = new CruxConfig(plugin, "structures/" + id);
        cfg.set("key", id);
        cfg.set("schematic", schematic);
        cfg.set("persistent", persists);
        cfg.set("type", type);
        cfg.save();
        StructureRegistries.STRUCTURES.register(cfg.deserialize(CfgFAWEStructure.class, ""));
        new MsgContainer("<green>Structure " + id + ", created!").use(sender);
        return 1;
    }

    public int remove(@NotNull CommandSourceStack source, String id){
        CommandSender sender = getExecutor(source);
        CruxConfig cfg = new CruxConfig(plugin, "structures/" + id);
        if(!cfg.file().exists()){
            new MsgContainer("<red>Structure " + id + ", does not exist!").use(sender);
            return 0;
        }
        cfg.file().delete();
        StructureRegistries.STRUCTURES.remove(Crux.key(id));
        new MsgContainer("<red>Structure " + id + ", deleted!").use(sender);
        return 1;
    }
}
