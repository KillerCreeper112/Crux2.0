package killercreepr.cruxentities.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.cruxentities.entity.CruxMob;
import killercreepr.cruxentities.registries.CruxEntityRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CruxMobArgument implements CustomArgumentType.Converted<CruxMob, Key> {
    @Override
    public @NotNull CruxMob convert(@NotNull Key nativeType) {
        return Objects.requireNonNull(CruxEntityRegistries.ENTITIES.get(nativeType));
    }

    @Override
    public @NotNull ArgumentType<Key> getNativeType() {
        return ArgumentTypes.key();
    }

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        for(CruxMob p : CruxEntityRegistries.ENTITIES){
            builder.suggest(p.key().asMinimalString());
        }
        return builder.buildFuture();
    }

}
