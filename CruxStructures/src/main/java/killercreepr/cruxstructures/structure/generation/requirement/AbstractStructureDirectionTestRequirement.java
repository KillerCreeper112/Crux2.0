package killercreepr.cruxstructures.structure.generation.requirement;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.cruxstructures.structure.Structure;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class AbstractStructureDirectionTestRequirement implements StructureRequirement {
    protected final @NotNull Map<BlockFace, NumberProvider> directions;
    protected final @NotNull NumberProvider minDirectionAmount;

    public AbstractStructureDirectionTestRequirement(@NotNull Map<BlockFace, NumberProvider> directions, @NotNull NumberProvider minDirectionAmount) {
        this.directions = directions;
        this.minDirectionAmount = minDirectionAmount;
    }

    @Override
    public boolean test(@NotNull Structure structure, @NotNull Chunk chunk, @NotNull Location location) {
        Block block = location.getBlock();

        int min = minDirectionAmount.value().intValue();
        int amount = 0;
        for(Map.Entry<BlockFace, NumberProvider> entry : directions.entrySet()){
            int x = entry.getValue().value().intValue();
            if(test(block, entry.getKey(), x)){
                amount++;
                if(amount >= min) return true;
            }
        }
        return amount >= min;
    }

    public boolean test(@NotNull Block b, @NotNull BlockFace face, int amount){
        while(amount > 0){
            amount--;
            b = b.getRelative(face);
            if(!isValidBlock(b)) return false;
        }
        return true;
    }

    public abstract boolean isValidBlock(@NotNull Block block);
}
