package killercreepr.cruxpotion.command.args;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.cruxpotion.potions.CruxPotion;
import killercreepr.cruxpotion.registries.CruxPotionRegistries;
import net.kyori.adventure.key.Key;
import net.minecraft.commands.synchronization.SuggestionProviders;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class CruxPotionArgument implements CustomArgumentType<CruxPotion, Key> {
    private static final CruxPotionArgument CRUX_POTION = new CruxPotionArgument();
    public static CruxPotionArgument cruxPotion(){
        return CRUX_POTION;
    }

    @Override
    public @NotNull CruxPotion parse(@NotNull StringReader reader) throws CommandSyntaxException {
        Key key = Key.key(reader.readString());
        return CruxPotionRegistries.POTIONS.get(key);
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
