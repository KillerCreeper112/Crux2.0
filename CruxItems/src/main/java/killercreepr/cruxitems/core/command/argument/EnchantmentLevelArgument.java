package killercreepr.cruxitems.core.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class EnchantmentLevelArgument implements CustomArgumentType.Converted<Integer, Integer> {
    @Override
    public @NotNull Integer convert(@NotNull Integer nativeType) {
        return nativeType;
    }

    @Override
    public @NotNull ArgumentType<Integer> getNativeType() {
        return IntegerArgumentType.integer();
    }

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        Enchantment ench = context.getLastChild().getArgument("enchant", Enchantment.class);
        for(int i = 1; i <= ench.getMaxLevel(); i++){
            builder.suggest(i);
        }
        return builder.buildFuture();
    }

}
