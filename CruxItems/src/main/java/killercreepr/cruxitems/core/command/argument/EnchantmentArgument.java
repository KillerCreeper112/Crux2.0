package killercreepr.cruxitems.core.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class EnchantmentArgument implements CustomArgumentType.Converted<Enchantment, Key> {
    @Override
    public @NotNull Enchantment convert(@NotNull Key nativeType) {
        return Objects.requireNonNull(
            RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT)
                .get(nativeType)
        );
    }

    @Override
    public @NotNull ArgumentType<Key> getNativeType() {
        return ArgumentTypes.key();
    }

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        for(var p : RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT)){
            builder.suggest(p.key().asString());
        }
        return builder.buildFuture();
    }

}
