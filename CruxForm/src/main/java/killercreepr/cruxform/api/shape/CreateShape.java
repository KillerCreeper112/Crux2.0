package killercreepr.cruxform.api.shape;

import killercreepr.crux.api.math.CruxPosition;
import killercreepr.cruxform.api.shape.cache.CreateCachedShape;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public interface CreateShape {
    void generate(@NotNull Consumer<CruxPosition> consumer);
    default @NotNull CreateCachedShape generateCache(){
        return generateCache(null);
    }
    @NotNull
    CreateCachedShape generateCache(@Nullable Consumer<Vector> consumer);
    default List<CruxPosition> generateList(){
        List<CruxPosition> list = new ArrayList<>();
        generate(list::add);
        return list;
    }
}
