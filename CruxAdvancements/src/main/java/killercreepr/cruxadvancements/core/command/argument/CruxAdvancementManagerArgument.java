package killercreepr.cruxadvancements.core.command.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import killercreepr.crux.core.command.argument.CruxKeyedArgument;
import killercreepr.cruxadvancements.api.advancement.manager.CruxAdvancementManager;
import killercreepr.cruxadvancements.core.registries.AdvancementRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CruxAdvancementManagerArgument implements CruxKeyedArgument<CruxAdvancementManager<?>> {
    @Override
    public @NotNull CruxAdvancementManager<?> parse(@NotNull Key key) {
        return Objects.requireNonNull(
            AdvancementRegistries.ADVANCEMENT_MANAGERS.get(key),
            "AdvancementManager " + key + " not found!"
        );
    }

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        for(CruxAdvancementManager<?> manager : AdvancementRegistries.ADVANCEMENT_MANAGERS){
            builder.suggest(manager.key().asString());
        }
        return builder.buildFuture();
    }
}
