package killercreepr.crux.core.command.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import killercreepr.crux.api.registry.KeyedRegistry;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class CruxKeyedRegistryArgument<T> implements CruxKeyedArgument<T>{
  public final KeyedRegistry<? extends T> registry;
  public final Function<Key, String> errorMsg;

  public CruxKeyedRegistryArgument(KeyedRegistry<? extends T> registry, Function<Key, String> errorMsg) {
    this.registry = registry;
    this.errorMsg = errorMsg;
  }
  public CruxKeyedRegistryArgument(KeyedRegistry<? extends T> registry) {
    this.registry = registry;
    this.errorMsg =id-> "No value found from " + id;
  }

  @Override
  public @NonNull T parse(@NotNull Key key) {
    return Objects.requireNonNull(registry.get(key),
      errorMsg.apply(key));
  }

  @Override
  public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
    registry.forEach(x -> builder.suggest(x.key().asString()));
    return builder.buildFuture();
  }
}
