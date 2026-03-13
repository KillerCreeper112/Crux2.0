package killercreepr.cruxstructures.core.structure.generation;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.LootTable;
import killercreepr.crux.core.util.FutureUtil;
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
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class CfgStructureGen implements StructureGenerator {
    protected final @NotNull LootTable<Key> structurePool;
    protected final @NotNull StructureCenter center;
    protected final @NotNull List<StructureRequirement> requirements;
    protected final @NotNull List<StructureChunkRequirement> chunkRequirements;
    public CfgStructureGen(@NotNull LootTable<Key> structurePool, @NotNull StructureCenter center, @NotNull List<StructureRequirement> requirements, @NotNull List<StructureChunkRequirement> chunkRequirements) {
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
    public boolean canPlace(@NotNull Chunk at) {
        for(StructureChunkRequirement requirement : chunkRequirements){
            if(!requirement.test(at)) return false;
        }
        return true;
    }

    @Override
    public @Nullable Structure generateStructure(@NotNull Chunk at) {
        List<Key> structureKey = structurePool.populateLoot(LootContext.builder()
          .info(DataExchange.builder().put("chunk", at).build())
          .build());
        if(structureKey.isEmpty()) return null;

      return StructureRegistries.STRUCTURES.get(structureKey.getFirst());
    }

    /*@Override
    public @NotNull CompletableFuture<GenerateResult> generate(@NotNull Chunk at){
        List<Key> structureKey = structurePool.populateLoot(LootContext.builder()
            .info(DataExchange.builder().put("chunk", at).build())
            .build());
        if(structureKey.isEmpty()) return GenerateResult.empty();

        Structure structure = StructureRegistries.STRUCTURES.get(structureKey.getFirst());
        if(structure==null) return GenerateResult.empty();
        return generate(structure, at);
    }

    @Override
    public @NotNull CompletableFuture<GenerateResult> generate(@NotNull Location at) {
        List<Key> structureKey = structurePool.populateLoot(LootContext.builder()
            .location(at)
            .build());
        if(structureKey.isEmpty()) return GenerateResult.empty();

        Structure structure = StructureRegistries.STRUCTURES.get(structureKey.getFirst());
        if(structure==null) return GenerateResult.empty();
        return generate(structure, at);
    }*/

    @Override
    public @NotNull CompletableFuture<GenerateResult> generate(@NotNull Structure structure, @NotNull Location at) {
        var chunk = at.getChunk();
        return FutureUtil.allMatchSequentially(
          requirements,
          requirement -> requirement.test(structure, chunk, at)
        ).thenApply(passed ->
          passed
            ? GenerateResult.result(structure.place(at))
            : GenerateResult.empty()
        );
    }

    @Override
    public @NotNull CompletableFuture<GenerateResult> generate(@NotNull Structure structure, @NotNull Chunk at) {
        Location l = center.scan(structure, at);
        if(l==null) return CompletableFuture.completedFuture(GenerateResult.empty());
        return generate(structure, l);
    }
}
