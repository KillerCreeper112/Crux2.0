package killercreepr.crux.core.util;

import com.destroystokyo.paper.MaterialSetTag;
import killercreepr.crux.core.data.util.Pair;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.type.Comparator;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.block.data.type.Repeater;
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

    public static Pair<Block, Integer> findPowerAndSource(Block center){
        Block block = findPowerSource(center);
        if(block == null) return null;
        int power;
        BlockData data = block.getBlockData();
        if(data instanceof AnaloguePowerable p) power = p.getPower();
        else power = 15;
        return Pair.of(block, power);
    }

    public static Block findPowerSource(Block center){
        for(BlockFace face : CruxBlockFace.CARTESIAN){
            Block check = center.getRelative(face);
            if(face != BlockFace.DOWN && face != BlockFace.UP){
                Block up = check.getRelative(BlockFace.UP);
                if((up.getBlockData() instanceof RedstoneWire || canPowerThrough(up, face)) && isPowered(up)){
                    return up;
                }
            }

            if(!canPowerThrough(check, face)) continue;
            if(!isPowered(check)) continue;
            return check;
        }
        //redstone torch check

        Block check = center.getRelative(0, -2, 0);
        var type = check.getType();
        if(type == Material.REDSTONE_TORCH || type == Material.REDSTONE_WALL_TORCH){
            if(isPowered(check)){
                Block inBetween = center.getRelative(0, -1, 0);
                if(inBetween.isSolid() && !inBetween.isLiquid()) return check;
            }
        }


        return null;
    }

    public static boolean canPowerThrough(Block check, BlockFace face){
        var type = check.getType();
        if(type == Material.REDSTONE_TORCH || type == Material.REDSTONE_WALL_TORCH){
            return face != BlockFace.UP && CruxBlockFace.CARTESIAN.contains(face);
        }
        if(type == Material.REDSTONE_BLOCK){
            return CruxBlockFace.CARTESIAN.contains(face);
        }
        if(type == Material.LEVER || MaterialSetTag.BUTTONS.isTagged(type) || type == Material.DAYLIGHT_DETECTOR){
            return CruxBlockFace.CARTESIAN.contains(face);
        }
        if(MaterialSetTag.PRESSURE_PLATES.isTagged(type)){
            return face == BlockFace.UP;
        }
        BlockData data = check.getBlockData();
        if(data instanceof RedstoneWire wire){
            if(!wire.getAllowedFaces().contains(face)) return false;
            var connection = wire.getFace(face.getOppositeFace());
            if(connection == RedstoneWire.Connection.NONE) return false;
            BlockFace dir = connection == RedstoneWire.Connection.UP ? BlockFace.DOWN : face;
            return dir == face;
        }
        if(data instanceof Repeater repeater){
            return repeater.getFacing() == face;
        }
        if(data instanceof Comparator comparator){
            return comparator.getFacing() == face;
        }
        return false;
    }

    public static boolean isPowered(Block block){
        BlockData data = block.getBlockData();
        if(data instanceof AnaloguePowerable powerable){
            return powerable.getPower() > 0;
        }
        if(data instanceof Powerable powerable){
            return powerable.isPowered();
        }
        var type = block.getType();
        if(type == Material.REDSTONE_BLOCK) return true;
        if(type == Material.REDSTONE_TORCH || type == Material.REDSTONE_WALL_TORCH){
            if(data instanceof Lightable light){
                return light.isLit();
            }
        }
        return false;
    }
}
