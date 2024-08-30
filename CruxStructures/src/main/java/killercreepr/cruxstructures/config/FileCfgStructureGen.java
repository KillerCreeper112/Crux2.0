package killercreepr.cruxstructures.config;

import com.google.common.reflect.TypeToken;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.structure.generation.center.StructureCenter;
import killercreepr.cruxstructures.structure.generation.requirement.StructureChunkRequirement;
import killercreepr.cruxstructures.structure.generation.requirement.StructureRequirement;
import killercreepr.cruxstructures.structure.impl.CfgStructureGen;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class FileCfgStructureGen extends PureYamlFileHandler<CfgStructureGen> {
    @Override
    public @Nullable CfgStructureGen deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = context.getRegistry();
        StructureCenter center = registry.deserializeFromFile(StructureCenter.class, o.get("center"));
        if(center==null){
            throw new RuntimeException("StructureCenter type not found!");
        }
        Key structureKey = registry.deserializeFromFile(Key.class, o.get("structure"));
        if(structureKey==null) return null;

        Collection<StructureRequirement> requirements = new HashSet<>();
        if(o.get("requirements") instanceof FileArray aa){
            aa.forEach(ele ->{
                StructureRequirement req = registry.deserializeFromFile(StructureRequirement.class, ele);
                if(req==null){
                    throw new RuntimeException("StructureRequirement type not found!");
                }
                requirements.add(req);
            });
        }

        Collection<StructureChunkRequirement> chunkRequirements = registry.deserializeFromFile(
            new TypeToken<Collection<StructureChunkRequirement>>(){}.getType(), o.get("chunk_requirements")
        );

        return new CfgStructureGen(
            structureKey, center, requirements, chunkRequirements == null ? Set.of() : chunkRequirements
        );
    }
}
