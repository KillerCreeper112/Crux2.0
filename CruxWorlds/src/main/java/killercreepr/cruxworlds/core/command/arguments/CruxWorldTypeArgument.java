package killercreepr.cruxworlds.core.command.arguments;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import killercreepr.crux.core.command.argument.CruxKeyedArgument;
import killercreepr.cruxworlds.api.world.type.CruxWorldType;
import killercreepr.cruxworlds.core.registries.WorldsRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CruxWorldTypeArgument implements CruxKeyedArgument<CruxWorldType> {

    @Override
    public @NotNull CruxWorldType parse(@NotNull Key key) {
        return Objects.requireNonNull(
            WorldsRegistries.WORLD_TYPE.get(key),
            "CruxWorldType " + key + " not found!"
        );
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        WorldsRegistries.WORLD_TYPE.forEach(type -> builder.suggest(type.key().asString()));
        return builder.buildFuture();
    }
}
