package killercreepr.cruxstructures.core.structure.pending;

import killercreepr.cruxstructures.api.structure.Structure;
import killercreepr.cruxstructures.api.structure.generation.StructureGenerator;

import java.util.HashMap;
import java.util.Map;

public class PendingStructure {
  public final StructureGenerator generator;
  public final Structure structure;
  public final int centerChunkX;
  public final int centerChunkZ;
  public final long centerChunkKey;

  public final Map<Long, Boolean> requiredChunks = new HashMap<>();

  public PendingStructure(StructureGenerator generator, Structure structure, int centerChunkX, int centerChunkZ, long centerChunkKey) {
    this.generator = generator;
    this.structure = structure;
    this.centerChunkX = centerChunkX;
    this.centerChunkZ = centerChunkZ;
    this.centerChunkKey = centerChunkKey;
  }

  public boolean canPlace(){
    for (Boolean value : requiredChunks.values()) {
      if(!value) return false;
    }
    return true;
  }
}
