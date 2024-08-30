package killercreepr.crux.block;

import killercreepr.crux.data.world.CruxPosition;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

//todo place block methods and document the differences
public interface CruxBlockWrapper {
    default void setBlock(@NotNull Location block){
        setBlock(block, true);
    }
    default void setBlock(@NotNull Location block, boolean applyPhysics){
        setBlock(block.getWorld(), CruxPosition.block(block), applyPhysics);
    }

    default void setBlock(@NotNull Block block){
        setBlock(block, true);
    }
    default void setBlock(@NotNull Block block, boolean applyPhysics){
        setBlock(block.getWorld(), CruxPosition.block(block), applyPhysics);
    }

    default void setBlock(@NotNull World world, @NotNull CruxPosition position){
        setBlock(world, position, true);
    }
    void setBlock(@NotNull World world, @NotNull CruxPosition position, boolean applyPhysics);

    class Vanilla implements CruxBlockWrapper {
        protected final @NotNull Material material;
        public Vanilla(@NotNull Material material) {
            this.material = material;
        }

        public @NotNull Material getMaterial() {
            return material;
        }

        @Override
        public void setBlock(@NotNull World world, @NotNull CruxPosition position, boolean applyPhysics) {
            position.getBlock(world).setType(material, applyPhysics);
        }
    }
}
