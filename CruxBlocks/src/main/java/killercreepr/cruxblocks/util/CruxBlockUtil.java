package killercreepr.cruxblocks.util;

import org.bukkit.block.Block;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

public class CruxBlockUtil {
    public static @NotNull BoundingBox getBlockBox(@NotNull Block b){
        return new BoundingBox(b.getX(), b.getY(), b.getZ(),
            b.getX() + 1, b.getY() + 1, b.getZ() + 1);
    }
}
