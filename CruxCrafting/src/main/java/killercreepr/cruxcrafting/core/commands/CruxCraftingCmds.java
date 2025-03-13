package killercreepr.cruxcrafting.core.commands;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import killercreepr.crux.core.command.CruxLootCommands;
import killercreepr.cruxcrafting.api.crafting.CruxRecipeManager;
import killercreepr.cruxcrafting.api.crafting.recipe.CruxRecipe;
import killercreepr.cruxcrafting.core.commands.arg.CraftingArgs;
import killercreepr.cruxcrafting.core.commands.arg.CruxRecipeResolver;
import org.bukkit.Keyed;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.List;

public class CruxCraftingCmds {
    protected final Plugin plugin;

    public CruxCraftingCmds(Plugin plugin) {
        this.plugin = plugin;
    }

    public void register(){
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, ctx ->{
            register(ctx.registrar());
        });
    }

    public void register(Commands commands){
        commands.register(
            Commands.literal("cruxcrafting")
                .requires(source -> source.getSender().hasPermission("cruxcrafting.cmds.cruxcrafting.use"))
                .then(
                    Commands.literal("revoke")
                        .then(
                            Commands.argument("targets", ArgumentTypes.entities())
                                .then(
                                    Commands.argument("recipe_manager", CraftingArgs.RECIPE_MANAGER)
                                        .then(
                                            Commands.argument("recipe", CraftingArgs.RECIPE)
                                                .executes(ctx ->{
                                                    CommandSender sender = CruxLootCommands.getExecutor(ctx.getSource());
                                                    Collection<Entity> targets = ctx.getArgument("targets",
                                                        EntitySelectorArgumentResolver.class).resolve(ctx.getSource());
                                                    CruxRecipeManager manager = ctx.getArgument("recipe_manager", CruxRecipeManager.class);
                                                    CruxRecipe recipe = ctx.getArgument("recipe", CruxRecipeResolver.class).resolve(manager);
                                                    String name = recipe instanceof Keyed d ? d.key().asString() : recipe.toString();
                                                    for(Entity e : targets){
                                                        boolean value = manager.revokeRecipe(e, recipe);
                                                        sender.sendMessage("Revoked recipe " + name + " from " + e.getName() + "?: " + value);
                                                    }
                                                    return 1;
                                                })
                                        )
                                )
                        )
                ).then(
                    Commands.literal("grant")
                        .then(
                            Commands.argument("targets", ArgumentTypes.entities())
                                .then(
                                    Commands.argument("recipe_manager", CraftingArgs.RECIPE_MANAGER)
                                        .then(
                                            Commands.argument("recipe", CraftingArgs.RECIPE)
                                                .executes(ctx ->{
                                                    CommandSender sender = CruxLootCommands.getExecutor(ctx.getSource());
                                                    Collection<Entity> targets = ctx.getArgument("targets",
                                                        EntitySelectorArgumentResolver.class).resolve(ctx.getSource());
                                                    CruxRecipeManager manager = ctx.getArgument("recipe_manager", CruxRecipeManager.class);
                                                    CruxRecipe recipe = ctx.getArgument("recipe", CruxRecipeResolver.class).resolve(manager);
                                                    String name = recipe instanceof Keyed d ? d.key().asString() : recipe.toString();
                                                    for(Entity e : targets){
                                                        boolean value = manager.grantRecipe(e, recipe);
                                                        sender.sendMessage("Granted recipe " + name + " from " + e.getName() + "?: " + value);
                                                    }
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )
                .build(), List.of("ccraft")
        );
    }
}
