package killercreepr.cruxadvancements.core.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.crux.core.command.argument.CruxKeyedArgument;
import killercreepr.cruxadvancements.api.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.api.advancement.manager.CruxAdvancementManager;
import killercreepr.cruxadvancements.core.data.AdvancementPair;
import killercreepr.cruxadvancements.core.registries.AdvancementRegistries;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CruxAdvancementKeysArgument implements CustomArgumentType.Converted<String, String> {
    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> ctx, @NotNull SuggestionsBuilder builder) {
        try{
            CruxAdvancementManager<?> manager = ctx.getLastChild().getLastChild().getArgument("manager", CruxAdvancementManager.class);
            CruxAdvancement advancement = ctx.getArgument("advancement", CruxAdvancementResolver.class)
                .resolve(manager);
            for (String s : advancement.getProgressMap().keySet()) {
                builder.suggest(s);
            }
        }catch (Exception ignored){
            var pair = ctx.getLastChild().getArgument("advancements", AdvancementPair.class);
            for (String s : pair.getAdvancement().getProgressMap().keySet()) {
                builder.suggest(s);
            }
        }
        return builder.buildFuture();
    }

    @Override
    public String convert(String nativeType) throws CommandSyntaxException {
        return nativeType;
    }
}
