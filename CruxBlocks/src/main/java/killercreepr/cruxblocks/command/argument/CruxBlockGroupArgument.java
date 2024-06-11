package killercreepr.cruxblocks.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.registeries.CruxBlocksRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CruxBlockGroupArgument implements CustomArgumentType.Converted<CruxBlockGroup, Key> {
    @Override
    public @NotNull CruxBlockGroup convert(@NotNull Key nativeType) {
        return Objects.requireNonNull(CruxBlocksRegistries.BLOCKS.getGroup(nativeType));
    }

    @Override
    public @NotNull ArgumentType<Key> getNativeType() {
        return ArgumentTypes.key();
    }

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        for(CruxBlockGroup p : CruxBlocksRegistries.BLOCKS.getGroups()){
            builder.suggest(p.key().asMinimalString());
        }
        return builder.buildFuture();
    }

}
