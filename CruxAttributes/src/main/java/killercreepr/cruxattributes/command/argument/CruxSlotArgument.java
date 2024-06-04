package killercreepr.cruxattributes.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.crux.Crux;
import killercreepr.cruxattributes.attribute.CruxSlot;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CruxSlotArgument implements CustomArgumentType<CruxSlot, StringArgumentType> {
    protected final ArgumentType<StringArgumentType> nativeType = reader -> StringArgumentType.string();
    @Override
    public @NotNull CruxSlot parse(@NotNull StringReader reader) throws CommandSyntaxException {
        return Objects.requireNonNull(
            CruxSlot.match(getNativeType().parse(reader).parse(reader))
        );
    }

    @Override
    public @NotNull ArgumentType<StringArgumentType> getNativeType() {
        return nativeType;
    }

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        for(CruxSlot s : CruxSlot.values()){
            builder.suggest(s.toString().toLowerCase());
        }
        return builder.buildFuture();
    }
}
