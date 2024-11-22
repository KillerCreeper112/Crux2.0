package killercreepr.crux.api.block.predicate;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface BlockPredicateComponent extends BlockPredicate {
    @NotNull
    List<String> encodeToParser();
}