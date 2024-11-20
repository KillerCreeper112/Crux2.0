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
import killercreepr.cruxadvancements.api.advancement.manager.CruxAdvancementManager;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CruxAdvancementListArgument implements CustomArgumentType<CruxAdvancementListResolver, Key> {

    @Override
    public @NotNull CruxAdvancementListResolver parse(@NotNull StringReader reader) throws CommandSyntaxException {
        if(reader.getString().equalsIgnoreCase("*")){
            return (manager) -> new ArrayList<>(manager.getAdvancements().values());
        }
        Key key = CruxCmdArguments.CRUX_KEY.parse(reader);
        return manager -> List.of(
            Objects.requireNonNull(manager.getAdvancement(key), "Advancement, " + key + " not found in " + manager.key() + "!")
        );
    }

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        CruxAdvancementManager<?> manager = context.getLastChild().getArgument("manager", CruxAdvancementManager.class);
        for(CruxAdvancement a : manager){
            builder.suggest(a.key().asString());
        }
        builder.suggest("*");
        return builder.buildFuture();
    }

    @Override
    public @NotNull ArgumentType<Key> getNativeType() {
        return ArgumentTypes.key();
    }
}
