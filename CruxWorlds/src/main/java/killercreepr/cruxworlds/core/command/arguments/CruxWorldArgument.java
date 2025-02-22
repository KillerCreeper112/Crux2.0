package killercreepr.cruxworlds.core.command.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import killercreepr.cruxworlds.api.world.CruxWorld;
import killercreepr.cruxworlds.api.world.manager.CruxWorldManager;
import net.kyori.adventure.key.Key;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class CruxWorldArgument implements CustomArgumentType.Converted<CruxWorld, Key> {
    protected final CruxWorldManager worldManager;
    public CruxWorldArgument(CruxWorldManager worldManager) {
        this.worldManager = worldManager;
    }

    @Override
    public CruxWorld convert(Key nativeType) throws CommandSyntaxException {
        return Objects.requireNonNull(worldManager.getWorld(nativeType), nativeType + " CruxWorld not found!");
    }

    @Override
    public ArgumentType<Key> getNativeType() {
        return ArgumentTypes.key();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        worldManager.getWorlds().forEach(world -> builder.suggest(world.key().asString()));
        return builder.buildFuture();
    }
}
