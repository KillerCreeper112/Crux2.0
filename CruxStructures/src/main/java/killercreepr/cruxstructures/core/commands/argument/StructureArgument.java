package killercreepr.cruxstructures.core.commands.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.cruxstructures.core.registries.StructureRegistries;
import killercreepr.cruxstructures.api.structure.Structure;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class StructureArgument implements CustomArgumentType.Converted<Structure, Key> {
    @Override
    public @NotNull Structure convert(@NotNull Key nativeType) {
        return Objects.requireNonNull(StructureRegistries.STRUCTURES.get(nativeType), "Structure '" + nativeType + "' not found!");
    }

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        for(Structure s : StructureRegistries.STRUCTURES){
            builder.suggest(s.key().asString());
        }
        return builder.buildFuture();
    }

    @Override
    public @NotNull ArgumentType<Key> getNativeType() {
        return ArgumentTypes.key();
    }
}
