package killercreepr.cruxstructures.structure.active;

import killercreepr.crux.data.world.BlockPosed;
import killercreepr.cruxstructures.structure.Structure;
import killercreepr.cruxstructures.structure.stored.StoredStructure;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ActiveStructure extends BlockPosed {
    @NotNull
    StoredStructure getData();
    default @NotNull Structure getStructure(){
        return getData().getParent();
    }
    @NotNull
    Block getCenter();
    @NotNull
    default Chunk getChunk(){
        return getCenter().getChunk();
    }

    default @Nullable StoredStructure save(){
        return getData();
    }
}
