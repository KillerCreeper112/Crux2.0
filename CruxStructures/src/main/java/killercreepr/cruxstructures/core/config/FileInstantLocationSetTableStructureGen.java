package killercreepr.cruxstructures.core.config;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxstructures.core.structure.generation.InstantLocationSetTableStructureGen;
import killercreepr.cruxstructures.core.structure.generation.LocationSetTableStructureGen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileInstantLocationSetTableStructureGen extends FileLocationSetTableStructureGen {
    @Override
    public @Nullable LocationSetTableStructureGen deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        LocationSetTableStructureGen gen = super.deserializeFromFile(ctx, e);
        if(!(e instanceof FileObject o) || gen == null) return gen;
        String id = o.getObject(String.class, "id");
        return new InstantLocationSetTableStructureGen(
            gen.getStructurePool(), gen.getStructureAmount(), gen.getChunkRangeX(),
            gen.getChunkRangeZ(), gen.getMinDistanceApart(), id
        );
    }
}
