package killercreepr.cruxstructures.config;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.core.Crux;
import killercreepr.crux.api.loot.LootTable;
import killercreepr.crux.api.registry.MappedRegistry;
import killercreepr.crux.core.registry.SimpleMappedRegistry;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.FileSimpleLootTable;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxstructures.CruxStructuresModule;
import killercreepr.cruxstructures.structure.generation.StructureGenerator;
import killercreepr.cruxstructures.structure.generation.center.StructureCenter;
import killercreepr.cruxstructures.structure.generation.impl.CfgStructureGen;
import killercreepr.cruxstructures.structure.generation.requirement.StructureChunkRequirement;
import killercreepr.cruxstructures.structure.generation.requirement.StructureRequirement;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class FileCfgStructureGen extends PureYamlFileHandler<StructureGenerator> {
    protected static final @NotNull FileSimpleLootTable<Key> fileSimpleLootTable = CruxStructuresModule.fileSimpleLootTable;
    private final MappedRegistry<String, FileObjectHandler<? extends StructureGenerator>> TYPE_HANDLERS = new SimpleMappedRegistry<>();
    public MappedRegistry<String, FileObjectHandler<? extends StructureGenerator>> typeHandlers(){
        return TYPE_HANDLERS;
    }
    @Override
    public @Nullable StructureGenerator deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = context.getRegistry();

        String type = o.getObject(String.class, "generator");
        if(type != null){
            FileObjectHandler<? extends StructureGenerator> handler = typeHandlers().get(type);
            if(handler != null){
                return handler.deserializeFromFile(context, e);
            }else Crux.log(Level.WARNING, "FileCfgStructureGen TYPE_HANDLER of " + type + " not found!. Attempting to parse default..");
        }

        StructureCenter center = registry.deserializeFromFile(StructureCenter.class, o.get("center"));
        if(center==null){
            throw new RuntimeException("StructureCenter type not found!");
        }
        LootTable<Key> structurePool = fileSimpleLootTable.deserializeFromFile(
            context, o.get("structure")
        );
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
