package killercreepr.cruxblocks.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.registries.CruxBlocksRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CruxBlockArgument implements CustomArgumentType.Converted<CruxBlock, Key> {
    @Override
    public @NotNull CruxBlock convert(@NotNull Key nativeType) {
        return Objects.requireNonNull(CruxBlocksRegistries.BLOCK.get(nativeType));
    }

    @Override
    public @NotNull ArgumentType<Key> getNativeType() {
        return ArgumentTypes.key();
    }

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        for(CruxBlock p : CruxBlocksRegistries.BLOCK){
            builder.suggest(p.key().asMinimalString());
        }
        return builder.buildFuture();
    }

}
