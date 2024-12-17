package killercreepr.cruxworlds.core.command.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.cruxworlds.api.world.CruxWorld;
import killercreepr.cruxworlds.api.world.manager.CruxWorldManager;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CruxWorldArgument implements CustomArgumentType.Converted<CruxWorld, String> {
    protected final CruxWorldManager worldManager;
    public CruxWorldArgument(CruxWorldManager worldManager) {
        this.worldManager = worldManager;
    }

    @Override
    public CruxWorld convert(String nativeType) throws CommandSyntaxException {
        return Objects.requireNonNull(worldManager.getWorld(nativeType), nativeType + " CruxWorld not found!");
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        worldManager.getWorlds().forEach(world -> builder.suggest(world.getName()));
        return builder.buildFuture();
    }
}
