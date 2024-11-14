package killercreepr.cruxstats.core.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.cruxstats.api.stat.CruxStat;

import java.util.concurrent.CompletableFuture;

public class CruxStatOperationArgument implements CustomArgumentType.Converted<CruxStat.Operation, String> {
    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for(CruxStat.Operation o : CruxStat.Operation.values()){
            builder.suggest(o.name().toLowerCase());
        }
        return builder.buildFuture();
    }

    @Override
    public CruxStat.Operation convert(String nativeType) throws CommandSyntaxException {
        return CruxStat.Operation.valueOf(nativeType.toUpperCase());
    }
}
