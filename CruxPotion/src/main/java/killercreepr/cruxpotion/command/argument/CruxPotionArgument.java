package killercreepr.cruxpotion.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.cruxpotion.potions.CruxPotion;
import killercreepr.cruxpotion.registries.CruxPotionRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CruxPotionArgument implements CustomArgumentType.Converted<CruxPotion, Key> {
    @Override
    public @NotNull CruxPotion convert(@NotNull Key nativeType) throws CommandSyntaxException {
        return Objects.requireNonNull(CruxPotionRegistries.POTIONS.get(nativeType));
    }

    @Override
    public @NotNull ArgumentType<Key> getNativeType() {
        return ArgumentTypes.key();
    }

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        for(CruxPotion p : CruxPotionRegistries.POTIONS){
            builder.suggest(p.key().asMinimalString());
        }
        return builder.buildFuture();
    }
}
