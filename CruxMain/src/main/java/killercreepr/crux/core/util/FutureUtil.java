package killercreepr.crux.core.util;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class FutureUtil {
  public static <T> CompletableFuture<Boolean> allMatchSequentially(
    List<T> items,
    Function<T, CompletableFuture<Boolean>> predicate
  ) {
    return allMatchSequentially(items, predicate, 0);
  }

  private static <T> CompletableFuture<Boolean> allMatchSequentially(
    List<T> items,
    Function<T, CompletableFuture<Boolean>> predicate,
    int index
  ) {
    if (index >= items.size()) {
      return CompletableFuture.completedFuture(true);
    }

    return predicate.apply(items.get(index)).thenCompose(passed -> {
      if (!passed) {
        return CompletableFuture.completedFuture(false);
      }
      return allMatchSequentially(items, predicate, index + 1);
    });
  }
}
