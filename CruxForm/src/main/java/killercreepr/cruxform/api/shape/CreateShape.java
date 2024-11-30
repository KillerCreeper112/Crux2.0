package killercreepr.cruxform.api.shape;

import killercreepr.crux.api.math.CruxLocation;
import killercreepr.cruxform.api.shape.cache.CreateCachedShape;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public interface CreateShape {
    void generate(@NotNull Consumer<CruxLocation> consumer);
    default @NotNull CreateCachedShape generateCache(){
        return generateCache(null);
    }
    @NotNull
    CreateCachedShape generateCache(@Nullable Consumer<Vector> consumer);
    default List<CruxLocation> generateList(){
        List<CruxLocation> list = new ArrayList<>();
        generate(list::add);
        return list;
    }
}
