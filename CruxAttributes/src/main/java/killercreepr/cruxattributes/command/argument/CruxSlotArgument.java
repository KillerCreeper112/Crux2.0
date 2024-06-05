package killercreepr.cruxattributes.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.cruxattributes.attribute.CruxSlot;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CruxSlotArgument implements CustomArgumentType<CruxSlot, String> {
    @Override
    public @NotNull CruxSlot parse(@NotNull StringReader reader) throws CommandSyntaxException {
        return Objects.requireNonNull(
            CruxSlot.match(getNativeType().parse(reader))
        );
    }

    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        for(CruxSlot s : CruxSlot.values()){
            builder.suggest(s.toString().toLowerCase());
        }
        return builder.buildFuture();
    }
}
