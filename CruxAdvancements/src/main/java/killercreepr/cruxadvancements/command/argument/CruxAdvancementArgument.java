package killercreepr.cruxadvancements.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.crux.command.argument.CruxCmdArguments;
import killercreepr.cruxadvancements.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CruxAdvancementArgument implements CustomArgumentType<CruxAdvancementResolver, Key> {

    @Override
    public @NotNull CruxAdvancementResolver parse(@NotNull StringReader reader) throws CommandSyntaxException {
        Key key = CruxCmdArguments.CRUX_KEY.parse(reader);
        return manager -> Objects.requireNonNull(manager.getAdvancement(key), "Advancement, " + key + " not found in " + manager.key() + "!");
    }

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        CruxAdvancementManager<?> manager = context.getLastChild().getArgument("manager", CruxAdvancementManager.class);
        for(CruxAdvancement a : manager){
            builder.suggest(a.key().asString());
        }
        return builder.buildFuture();
    }

    @Override
    public @NotNull ArgumentType<Key> getNativeType() {
        return ArgumentTypes.key();
    }
}
