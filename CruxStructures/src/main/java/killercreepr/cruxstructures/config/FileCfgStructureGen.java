package killercreepr.cruxstructures.config;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.Crux;
import killercreepr.crux.loot.LootTable;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.FileSimpleLootTable;
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
import java.util.logging.Level;

public class FileCfgStructureGen extends PureYamlFileHandler<CfgStructureGen> {
    protected final @NotNull FileSimpleLootTable<Key> fileSimpleLootTable = new FileSimpleLootTable<>(Key.class);
    @Override
    public @Nullable CfgStructureGen deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = context.getRegistry();
        StructureCenter center = registry.deserializeFromFile(StructureCenter.class, o.get("center"));
        if(center==null){
            throw new RuntimeException("StructureCenter type not found!");
        }
        LootTable<Key> structurePool = fileSimpleLootTable.deserializeFromFile(
            context, o.get("structure")
        );
        Crux.log(Level.WARNING, "structurePool: " + structurePool);
        if(structurePool==null) return null;

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
            structurePool, center, requirements, chunkRequirements == null ? Set.of() : chunkRequirements
        );
    }
}
