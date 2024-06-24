package killercreepr.crux.util;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxBlockUtil {
    public static boolean isValid(@NotNull BlockState state){
        Block b = state.getBlock();
        return b.getState().equals(state);
    }

    public static <T extends BlockState> @Nullable T updateState(@NotNull T state){
        BlockState test = state.getBlock().getState();
        if(state.getClass().isAssignableFrom(test.getClass())){
            return (T) test;
        }
        return null;
    }
    public static @NotNull BoundingBox getBlockBox(@NotNull Block b){
        return new BoundingBox(b.getX(), b.getY(), b.getZ(),
            b.getX() + 1, b.getY() + 1, b.getZ() + 1);
    }
}
