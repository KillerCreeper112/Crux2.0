package killercreepr.cruxitems.core.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.cruxitems.api.item.plugin.PluginItem;
import killercreepr.cruxitems.core.registries.CruxItemRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CruxPluginItemArgument implements CustomArgumentType.Converted<PluginItem, Key> {
    @Override
    public @NotNull PluginItem convert(@NotNull Key nativeType) {
        return Objects.requireNonNull(CruxItemRegistries.ITEMS.get(nativeType));
    }

    @Override
    public @NotNull ArgumentType<Key> getNativeType() {
        return ArgumentTypes.key();
    }

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        for(PluginItem p : CruxItemRegistries.ITEMS){
            builder.suggest(p.key().asMinimalString());
        }
        return builder.buildFuture();
    }

}
