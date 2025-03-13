package killercreepr.cruxcrafting.core.commands.arg;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.cruxcrafting.api.crafting.CruxRecipeManager;
import killercreepr.cruxcrafting.core.registries.CruxCraftingRegistries;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CruxRecipeManagerArg implements CustomArgumentType.Converted<CruxRecipeManager<?>, Key> {
    /**
     * Converts the value from the native type to the custom argument type.
     *
     * @param nativeType native argument provided value
     * @return converted value
     * @throws CommandSyntaxException if an exception occurs while parsing
     * @see #convert(Object, Object)
     */
    @Override
    public CruxRecipeManager<?> convert(Key nativeType) throws CommandSyntaxException {
        return Objects.requireNonNull(
            CruxCraftingRegistries.RECIPE_MANAGER.get(nativeType),
            "CruxRecipeManager of " + nativeType + " not found!"
        );
    }

    /**
     * Gets the native type that this argument uses,
     * the type that is sent to the client.
     *
     * @return native argument type
     */
    @Override
    public ArgumentType<Key> getNativeType() {
        return ArgumentTypes.key();
    }

    /**
     * Provides a list of suggestions to show to the client.
     *
     * @param context command context
     * @param builder suggestion builder
     * @return suggestions
     */
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (CruxRecipeManager<?> manager : CruxCraftingRegistries.RECIPE_MANAGER) {
            if(manager instanceof Keyed k) builder.suggest(k.key().asString());
        }
        return builder.buildFuture();
    }
}
