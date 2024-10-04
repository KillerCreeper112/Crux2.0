package killercreepr.cruxstructures.config;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxstructures.structure.impl.InstantLocationSetStructureGen;
import killercreepr.cruxstructures.structure.impl.LocationSetStructureGen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileInstantLocationSetStructureGen extends FileLocationSetStructureGen {
    @Override
    public @Nullable LocationSetStructureGen deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        LocationSetStructureGen gen = super.deserializeFromFile(ctx, e);
        if(!(e instanceof FileObject o) || gen == null) return gen;
        String id = o.getObject(String.class, "id");
        return new InstantLocationSetStructureGen(
            gen.getStructurePool(), gen.getStructureAmount(), gen.getChunkRangeX(),
            gen.getChunkRangeZ(), gen.getMinDistanceApart(), id
        );
    }
}
