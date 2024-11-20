package killercreepr.crux.core.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.crux.api.plugin.module.CruxModule;
import killercreepr.crux.core.registries.CruxRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CruxModuleArgument implements CustomArgumentType.Converted<CruxModule, String> {
    /**
     * Converts the value from the native type to the custom argument type.
     *
     * @param nativeType native argument provided value
     * @return converted value
     * @throws CommandSyntaxException if an exception occurs while parsing
     */
    @Override
    public @NotNull CruxModule convert(@NotNull String nativeType) throws CommandSyntaxException {
        return Objects.requireNonNull(CruxRegistries.MODULES.get(nativeType), "CruxModule " + nativeType + ", not found!");
    }

    /**
     * Gets the native type that this argument uses,
     * the type that is sent to the client.
     *
     * @return native argument type
     */
    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }

    /**
     * Provides a list of suggestions to show to the client.
     *
     * @param context command context
     * @param builder suggestion builder
     * @return suggestions
     */
    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        for(CruxModule m : CruxRegistries.MODULES){
            builder.suggest(m.name());
        }
        return builder.buildFuture();
    }
}
