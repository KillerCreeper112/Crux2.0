package killercreepr.cruxstructures.api.component;

import killercreepr.crux.api.math.CruxPosition;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface StoredBlocks {
    @NotNull Collection<CruxPosition> getStoredBlocks();
}
