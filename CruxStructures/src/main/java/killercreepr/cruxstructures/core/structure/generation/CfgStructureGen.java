package killercreepr.cruxstructures.core.structure.generation;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.LootTable;
import killercreepr.cruxstructures.api.structure.Structure;
import killercreepr.cruxstructures.api.structure.generation.StructureCenter;
import killercreepr.cruxstructures.api.structure.generation.StructureChunkRequirement;
import killercreepr.cruxstructures.api.structure.generation.StructureGenerator;
import killercreepr.cruxstructures.api.structure.generation.StructureRequirement;
import killercreepr.cruxstructures.api.structure.generation.result.GenerateResult;
import killercreepr.cruxstructures.core.registries.StructureRegistries;
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

    @Override
    public @NotNull GenerateResult generate(@NotNull Chunk at){
        List<Key> structureKey = structurePool.populateLoot(LootContext.builder()
            .info(DataExchange.builder().put("chunk", at).build())
            .build());
        if(structureKey.isEmpty()) return GenerateResult.empty();

        Structure structure = StructureRegistries.STRUCTURES.get(structureKey.getFirst());
        if(structure==null) return GenerateResult.empty();
        return generate(structure, at);
    }

    @Override
    public @NotNull GenerateResult generate(@NotNull Location at) {
        List<Key> structureKey = structurePool.populateLoot(LootContext.builder()
            .location(at)
            .build());
        if(structureKey.isEmpty()) return GenerateResult.empty();

        Structure structure = StructureRegistries.STRUCTURES.get(structureKey.getFirst());
        if(structure==null) return GenerateResult.empty();
        return generate(structure, at);
    }

    @Override
    public @NotNull GenerateResult generate(@NotNull Structure structure, @NotNull Location at) {
        for(StructureRequirement requirement : requirements){
            if(!requirement.test(structure, at.getChunk(), at)) return GenerateResult.empty();
        }
        return GenerateResult.result(structure.place(at));
    }

    @Override
    public @NotNull GenerateResult generate(@NotNull Structure structure, @NotNull Chunk at) {
        for(StructureChunkRequirement requirement : chunkRequirements){
            if(!requirement.test(structure, at)) return GenerateResult.empty();
        }

        Location l = center.scan(structure, at);
        if(l==null) return GenerateResult.empty();
        for(StructureRequirement requirement : requirements){
            if(!requirement.test(structure, at, l)) return GenerateResult.empty();
        }
        return GenerateResult.result(structure.place(l));
    }
}
