package killercreepr.cruxstructures.config;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxstructures.structure.generation.impl.InstantLocationSetListStructureGen;
import killercreepr.cruxstructures.structure.generation.impl.LocationSetListStructureGen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileInstantLocationSetListStructureGen extends FileLocationSetListStructureGen {
    @Override
    public @Nullable LocationSetListStructureGen deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        LocationSetListStructureGen gen = super.deserializeFromFile(ctx, e);
        if(!(e instanceof FileObject o) || gen == null) return gen;
        String id = o.getObject(String.class, "id");
        return new InstantLocationSetListStructureGen(
            gen.getStructurePool(), gen.getChunkRangeX(),
            gen.getChunkRangeZ(), gen.getMinDistanceApart(), id
        );
    }
}
