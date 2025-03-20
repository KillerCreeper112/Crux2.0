package killercreepr.cruxstatistics.core.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.cruxstatistics.api.statistic.CruxStatisticType;
import killercreepr.cruxstatistics.core.registries.CruxStatisticRegistries;
import net.kyori.adventure.key.Key;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CruxStatisticTypeArgument implements CustomArgumentType.Converted<CruxStatisticType<?>, Key> {

    @Override
    public CruxStatisticType<?> convert(Key nativeType) throws CommandSyntaxException {
        return Objects.requireNonNull(
            CruxStatisticRegistries.STATISTIC_TYPE.get(nativeType),
            "CruxStatisticType of " + nativeType + " not found!"
        );
    }

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
        for (CruxStatisticType<?> type : CruxStatisticRegistries.STATISTIC_TYPE) {
            builder.suggest(type.key().asString());
        }
        return builder.buildFuture();
    }
}
