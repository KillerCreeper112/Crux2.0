package killercreepr.cruxstructures.structure.impl;

import killercreepr.cruxstructures.registries.StructureRegistries;
import killercreepr.cruxstructures.structure.*;
import killercreepr.cruxstructures.structure.generation.StructureGenerator;
import killercreepr.cruxstructures.structure.generation.center.StructureCenter;
import killercreepr.cruxstructures.structure.generation.requirement.StructureRequirement;
import net.kyori.adventure.key.Key;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class CfgStructureGen implements StructureGenerator {
    protected final @NotNull Key structureKey;
    protected final @NotNull StructureCenter center;
    protected final @NotNull Collection<StructureRequirement> requirements;
    public CfgStructureGen(@NotNull Key structureKey, @NotNull StructureCenter center, @NotNull Collection<StructureRequirement> requirements) {
        this.structureKey = structureKey;
        this.center = center;
        this.requirements = requirements;
    }

    public @NotNull GenerateResult generate(@NotNull Chunk at){
        Structure structure = StructureRegistries.STRUCTURES.get(structureKey);
        if(structure==null) return new GenerateResult(null);
        return generate(structure, at);
    }

    @Override
    public @NotNull GenerateResult generate(@NotNull Structure structure, @NotNull Chunk at) {
        Location l = center.scan(structure, at);
        if(l==null) return new GenerateResult(null);
        for(StructureRequirement requirement : requirements){
            if(!requirement.test(structure, at, l)) return new GenerateResult(null);
        }
        return new GenerateResult(structure.place(l));
    }
}
