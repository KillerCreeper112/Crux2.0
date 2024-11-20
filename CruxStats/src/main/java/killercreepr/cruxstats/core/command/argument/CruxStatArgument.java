package killercreepr.cruxstats.core.command.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import killercreepr.crux.core.command.argument.CruxKeyedArgument;
import killercreepr.cruxstats.api.stat.CruxStat;
import killercreepr.cruxstats.core.registries.CruxStatRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CruxStatArgument implements CruxKeyedArgument<CruxStat> {
    @Override
    public @NotNull CruxStat parse(@NotNull Key key) {
        return Objects.requireNonNull(CruxStatRegistries.STAT.get(key), "Stat of " + key + " not found!");
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        CruxStatRegistries.STAT.forEach(stat ->{
            builder.suggest(stat.key().asString());
        });
        return builder.buildFuture();
    }
}
