package killercreepr.cruxstructures.core.config.generation;

import killercreepr.crux.core.util.CruxObjects;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.core.structure.generation.requirement.StructureChunkNoiseRequirement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileChunkNoiseRequirement extends PureYamlFileHandler<StructureChunkNoiseRequirement> {
    @Override
    public @Nullable StructureChunkNoiseRequirement deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        Float min = o.getObject(Float.class, "min");
        Float max = o.getObject(Float.class, "max");
        Float noiseFrequency = o.getObject(Float.class, "noise_frequency", .05f);
        Integer noiseOctaves = o.getObject(Integer.class, "noise_octaves", 2);
        if(CruxObjects.checkNull(min, max, noiseFrequency, noiseOctaves)) return null;
        return new StructureChunkNoiseRequirement(
            min, max, noiseFrequency, noiseOctaves
        );
    }

}
