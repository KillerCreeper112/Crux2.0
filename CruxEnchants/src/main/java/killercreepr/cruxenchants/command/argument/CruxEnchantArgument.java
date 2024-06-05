package killercreepr.cruxenchants.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.cruxenchants.enchant.CruxEnchant;
import killercreepr.cruxenchants.registries.CruxEnchantRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CruxEnchantArgument implements CustomArgumentType.Converted<CruxEnchant, Key> {
    @Override
    public @NotNull CruxEnchant convert(@NotNull Key nativeType) throws CommandSyntaxException {
        return Objects.requireNonNull(CruxEnchantRegistries.ENCHANTS.get(nativeType));
    }

    @Override
    public @NotNull ArgumentType<Key> getNativeType() {
        return ArgumentTypes.key();
    }

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        for(CruxEnchant p : CruxEnchantRegistries.ENCHANTS){
            builder.suggest(p.key().asMinimalString());
        }
        return builder.buildFuture();
    }

}
