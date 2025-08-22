package killercreepr.crux.core.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.crux.core.util.CruxTimeUtil;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public class InstantArgument implements CustomArgumentType.Converted<Instant, String> {

    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }
    @Override
    public Instant convert(String nativeType) throws CommandSyntaxException {
        return CruxTimeUtil.parseInstant(nativeType);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
        builder.suggest("2025-12-31");
        builder.suggest("March 24 12:00");
        builder.suggest("tomorrow");
        builder.suggest("in 5 minutes");
        builder.suggest("next week");
        builder.suggest("next saturday");
        builder.suggest("next friday 18:00");
        builder.suggest("next friday 18:00 EST");
        builder.suggest("August 24 12:00 EST");
        builder.suggest("tomorrow 14:00");
        builder.suggest("next monday 9:30 AM PST");
        builder.suggest("in 3 days 15:00");
        return builder.buildFuture();
    }
}
