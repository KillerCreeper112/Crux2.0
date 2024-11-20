package killercreepr.crux.core.command.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import killercreepr.crux.api.loot.LootTable;
import killercreepr.crux.api.loot.item.ItemLootTable;
import killercreepr.crux.core.registries.CruxRegistries;
import net.kyori.adventure.key.Key;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ItemLootTableArgument implements CruxKeyedArgument<ItemLootTable> {
    @Override
    public @NotNull ItemLootTable parse(@NotNull Key key) {
        return Objects.requireNonNull(
            (ItemLootTable) CruxRegistries.ITEM_LOOT_TABLE.get(key),
            "ItemLootTable " + key + " not found!"
        );
    }

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        for(LootTable<ItemStack> manager : CruxRegistries.ITEM_LOOT_TABLE){
            builder.suggest(manager.key().asString());
        }
        return builder.buildFuture();
    }
}
