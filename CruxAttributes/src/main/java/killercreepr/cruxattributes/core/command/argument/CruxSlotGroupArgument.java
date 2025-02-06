package killercreepr.cruxattributes.core.command.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import killercreepr.crux.core.command.argument.CruxKeyedArgument;
import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import killercreepr.cruxattributes.core.registries.CruxAttributeRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CruxSlotGroupArgument implements CruxKeyedArgument<CruxSlotGroup> {

    @Override
    public @NotNull CruxSlotGroup parse(@NotNull Key key) {
        return Objects.requireNonNull(
            CruxAttributeRegistries.SLOT_GROUP.get(key)
        );
    }

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        for(CruxSlotGroup s : CruxAttributeRegistries.SLOT_GROUP){
            builder.suggest(s.toString().toLowerCase());
        }
        return builder.buildFuture();
    }
}
