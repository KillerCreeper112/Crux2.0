package killercreepr.cruxadvancements.core.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.crux.core.command.argument.CruxCmdArguments;
import killercreepr.cruxadvancements.api.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.core.data.AdvancementPair;
import killercreepr.cruxadvancements.core.registries.AdvancementRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CruxAdvancementPairArgument implements CustomArgumentType<AdvancementPair, Key> {

    @Override
    public @NotNull AdvancementPair parse(@NotNull StringReader reader) throws CommandSyntaxException {
        Key key = CruxCmdArguments.CRUX_KEY.parse(reader);
        AdvancementPair pair = AdvancementPair.pair(key);
        Objects.requireNonNull(pair.getAdvancement(), "Advancement, " + key + " not found in " + pair.getManagerKey() + "!");
        return pair;
    }

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        for(var manager : AdvancementRegistries.ADVANCEMENT_MANAGERS){
            for(CruxAdvancement a : manager){
                builder.suggest(a.key().asString());
            }
        }
        return builder.buildFuture();
    }

    @Override
    public @NotNull ArgumentType<Key> getNativeType() {
        return ArgumentTypes.key();
    }
}
