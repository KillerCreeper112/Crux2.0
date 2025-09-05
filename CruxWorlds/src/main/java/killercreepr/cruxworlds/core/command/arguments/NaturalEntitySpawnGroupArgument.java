package killercreepr.cruxworlds.core.command.arguments;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import killercreepr.crux.core.command.argument.CruxKeyedArgument;
import killercreepr.cruxworlds.api.world.entity.NaturalEntitySpawnGroup;
import killercreepr.cruxworlds.core.registries.WorldsRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class NaturalEntitySpawnGroupArgument implements CruxKeyedArgument<NaturalEntitySpawnGroup> {

    @Override
    public @NotNull NaturalEntitySpawnGroup parse(@NotNull Key key) {
        return Objects.requireNonNull(
            WorldsRegistries.NATURAL_ENTITY_SPAWN_GROUP.get(key),
            "NaturalEntitySpawnGroup " + key + " not found!"
        );
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        WorldsRegistries.NATURAL_ENTITY_SPAWN_GROUP.forEach(type -> builder.suggest(type.key().asString()));
        return builder.buildFuture();
    }
}
