package killercreepr.cruxstructures.config;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.structure.generation.StructureGenerator;
import killercreepr.cruxstructures.structure.generation.impl.LocationSetListStructureGen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FileLocationSetListStructureGen extends PureYamlFileHandler<LocationSetListStructureGen> {
    @Override
    public @Nullable LocationSetListStructureGen deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        List<StructureGenerator> generators = registry.deserializeFromFile(
            new TypeToken<List<StructureGenerator>>(){}.getType(), o.get("generators")
        );
        if(generators == null) return null;
        NumberProvider chunkRange = registry.deserializeFromFile(NumberProvider.class, o.get("chunk_range"));
        NumberProvider chunkRangeX = registry.deserializeFromFile(NumberProvider.class, o.get("chunk_range_x"));
        NumberProvider chunkRangeZ = registry.deserializeFromFile(NumberProvider.class, o.get("chunk_range_z"));
        if(chunkRangeX == null) chunkRangeX = chunkRange;
        if(chunkRangeZ == null) chunkRangeZ = chunkRange;
        NumberProvider minDistanceApart = registry.deserializeFromFile(NumberProvider.class, o.get("minimum_distance_apart"));
        if(minDistanceApart == null) minDistanceApart = registry.deserializeFromFile(NumberProvider.class, o.get("min_distance_apart"));
        return new LocationSetListStructureGen(generators, chunkRangeX, chunkRangeZ, minDistanceApart);
    }
}
