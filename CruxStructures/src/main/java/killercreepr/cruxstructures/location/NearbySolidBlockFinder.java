package killercreepr.cruxstructures.location;

import killercreepr.crux.valueproviders.number.NumberProvider;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NearbySolidBlockFinder implements LocationFinder{
    protected final @NotNull NumberProvider maxYCheck;
    public NearbySolidBlockFinder(@NotNull NumberProvider maxYCheck) {
        this.maxYCheck = maxYCheck;
    }

    @Override
    public @Nullable Location find(@NotNull Location point) {
        Block b = findValidBlock(point.getBlock());
        if(b == null) return null;
        return b.getLocation();
    }

    public boolean isValidBlock(@NotNull Block b){
        if(!b.isSolid()) return false;
        Block check = b.getRelative(BlockFace.UP);
        return check.isEmpty() || check.isPassable() || check.isReplaceable();
    }

    public @Nullable Block findValidBlock(@NotNull Block start){
        if(isValidBlock(start)) return start;
        int max = maxYCheck.value().intValue();
        for(int y = 1; y <= max; y++){
            Block check = start.getRelative(0, y, 0);
            if(isValidBlock(check)) return check;
        }

        for(int y = -1; y >= -max; y--){
            Block check = start.getRelative(0, y, 0);
            if(isValidBlock(check)) return check;
        }
        return null;
    }
}
