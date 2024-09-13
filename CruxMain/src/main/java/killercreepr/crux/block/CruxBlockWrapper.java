package killercreepr.crux.block;

import killercreepr.crux.Crux;
import killercreepr.crux.data.Holder;
import killercreepr.crux.data.world.CruxPosition;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

//todo place block methods and document the differences
public interface CruxBlockWrapper {
    static CruxBlockWrapper reference(@NotNull Key key){
        return new Reference(key);
    }
    static CruxBlockWrapper material(@NotNull Material type){
        return new Vanilla(type);
    }
    static CruxBlockWrapper blockData(@NotNull BlockData data){
        return new VanillaData(data);
    }

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

    class Reference implements CruxBlockWrapper {
        protected final @NotNull Key key;
        public Reference(@NotNull Key key) {
            this.key = key;
        }

        @Override
        public void setBlock(@NotNull World world, @NotNull CruxPosition position, boolean applyPhysics) {
            CruxBlockWrapper wrapper = Crux.handlers().block().getBlockWrapper(key);
            Objects.requireNonNull(wrapper, "Block wrapper " + key + " not found!");
            wrapper.setBlock(world, position, applyPhysics);
        }
    }

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

    class VanillaData implements CruxBlockWrapper{
        protected final @NotNull BlockData data;
        public VanillaData(@NotNull BlockData data) {
            this.data = data;
        }

        public @NotNull BlockData getData() {
            return data;
        }

        @Override
        public void setBlock(@NotNull World world, @NotNull CruxPosition position, boolean applyPhysics) {
            Block block = position.getBlock(world);
            block.setType(data.getMaterial(), false);
            block.setBlockData(data, applyPhysics);
        }
    }
}
