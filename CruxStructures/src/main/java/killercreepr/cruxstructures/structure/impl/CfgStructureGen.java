package killercreepr.cruxstructures.structure.impl;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.LootTable;
import killercreepr.cruxstructures.registries.StructureRegistries;
import killercreepr.cruxstructures.structure.GenerateResult;
import killercreepr.cruxstructures.structure.Structure;
import killercreepr.cruxstructures.structure.generation.StructureGenerator;
import killercreepr.cruxstructures.structure.generation.center.StructureCenter;
import killercreepr.cruxstructures.structure.generation.requirement.StructureChunkRequirement;
import killercreepr.cruxstructures.structure.generation.requirement.StructureRequirement;
import net.kyori.adventure.key.Key;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class CfgStructureGen implements StructureGenerator {
    protected final @NotNull LootTable<Key> structurePool;
    protected final @NotNull StructureCenter center;
    protected final @NotNull Collection<StructureRequirement> requirements;
    protected final @NotNull Collection<StructureChunkRequirement> chunkRequirements;
    public CfgStructureGen(@NotNull LootTable<Key> structurePool, @NotNull StructureCenter center, @NotNull Collection<StructureRequirement> requirements, @NotNull Collection<StructureChunkRequirement> chunkRequirements) {
        this.structurePool = structurePool;
        this.center = center;
        this.requirements = requirements;
        this.chunkRequirements = chunkRequirements;
    }

    public @NotNull LootTable<Key> getStructurePool() {
        return structurePool;
    }

    public @NotNull StructureCenter getCenter() {
        return center;
    }

    public @NotNull Collection<StructureRequirement> getRequirements() {
        return requirements;
    }

    public @NotNull Collection<StructureChunkRequirement> getChunkRequirements() {
        return chunkRequirements;
    }

    public @NotNull GenerateResult generate(@NotNull Chunk at){
        List<Key> structureKey = structurePool.populateLoot(LootContext.builder()
            .info(DataExchange.builder().put("chunk", at).build())
            .build());
        if(structureKey.isEmpty()) return new GenerateResult(null);

        Structure structure = StructureRegistries.STRUCTURES.get(structureKey.getFirst());
        if(structure==null) return new GenerateResult(null);
        return generate(structure, at);
    }

    @Override
    public @NotNull GenerateResult generate(@NotNull Structure structure, @NotNull Chunk at) {
        for(StructureChunkRequirement requirement : chunkRequirements){
            if(!requirement.test(structure, at)) return new GenerateResult(null);
        }

        Location l = center.scan(structure, at);
        if(l==null) return new GenerateResult(null);
        for(StructureRequirement requirement : requirements){
            if(!requirement.test(structure, at, l)) return new GenerateResult(null);
        }
        return new GenerateResult(structure.place(l));
    }
}
