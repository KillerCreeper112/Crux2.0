package killercreepr.cruxitems.core.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.crux.core.Crux;
import killercreepr.crux.paper.ItemHolder;
import killercreepr.cruxitems.api.item.plugin.PluginItem;
import killercreepr.cruxitems.core.registries.CruxItemRegistries;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ItemHolderArgument implements CustomArgumentType.Converted<ItemHolder, Key> {
    @Override
    public @NotNull ItemHolder convert(@NotNull Key nativeType) {
        return Objects.requireNonNull(Crux.handlers().item().getItem(nativeType));
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
        for(Material m : Registry.MATERIAL){
            if(!m.isItem()) continue;
            builder.suggest(m.key().asString());
        }
        return builder.buildFuture();
    }

}
