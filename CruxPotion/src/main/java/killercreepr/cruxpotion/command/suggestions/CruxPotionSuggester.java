package killercreepr.cruxpotion.command.suggestions;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import killercreepr.cruxpotion.potions.CruxPotion;
import killercreepr.cruxpotion.registries.CruxPotionRegistries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class CruxPotionSuggester implements SuggestionProvider<CommandSourceStack> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        Collection<Suggestion> list = new ArrayList<>();
        for(CruxPotion p : CruxPotionRegistries.POTIONS){
            list.add(new Suggestion(StringRange.at(10), p.key().asMinimalString()));
        }
        return CompletableFuture.completedFuture(
                Suggestions.create("cruxpotion", list)
        );
    }
}
