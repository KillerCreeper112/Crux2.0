package killercreepr.cruxform.api.shape;

import killercreepr.crux.api.math.CruxLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public interface CreateShape {
    void generate(@NotNull Consumer<CruxLocation> consumer);
    default List<CruxLocation> generateList(){
        List<CruxLocation> list = new ArrayList<>();
        generate(list::add);
        return list;
    }
}
