package killercreepr.cruxstructures.config;

import killercreepr.cruxconfig.config.bukkit.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.bukkit.handler.YamlFileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxstructures.structure.StructureCenter;
import killercreepr.cruxstructures.structure.StructureRequirement;
import killercreepr.cruxstructures.structure.impl.CfgStructureGen;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;

public class FileCfgStructureGen extends YamlFileHandler<CfgStructureGen> {
    @Override
    public @Nullable CfgStructureGen deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = context.getRegistry();
        StructureCenter center = registry.deserialize(StructureCenter.class, o.get("center"));
        if(center==null){
            throw new RuntimeException("StructureCenter type not found!");
        }
        Key structureKey = registry.deserialize(Key.class, o.get("structure"));
        if(structureKey==null) return null;

        Collection<StructureRequirement> requirements = new HashSet<>();
        if(o.get("requirements") instanceof FileArray aa){
            aa.forEach(ele ->{
                StructureRequirement req = registry.deserialize(StructureRequirement.class, ele);
                if(req==null){
                    throw new RuntimeException("StructureRequirement type not found!");
                }
                requirements.add(req);
            });
        }

        return new CfgStructureGen(structureKey, center, requirements);
    }
}
