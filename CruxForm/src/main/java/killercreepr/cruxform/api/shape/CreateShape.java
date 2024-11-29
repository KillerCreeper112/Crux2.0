package killercreepr.cruxform.api.shape;

import killercreepr.crux.api.math.CruxLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface CreateShape {
    void generate(@NotNull Consumer<CruxLocation> consumer);
}
