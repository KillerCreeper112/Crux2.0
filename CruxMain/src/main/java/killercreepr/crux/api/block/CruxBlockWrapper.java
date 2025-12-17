package killercreepr.crux.api.block;

import killercreepr.crux.api.component.DataComponentAccessor;
import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.crux.api.component.serialization.PersistHolderComponentHandler;
import killercreepr.crux.api.component.serialization.PersistentDataWrappers;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.Crux;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

//todo place block methods and document the differences
public interface CruxBlockWrapper {
    static CruxBlockWrapper reference(@NotNull String key){
        return reference(key, null);
    }
    static CruxBlockWrapper reference(@NotNull String key, DataComponentAccessor components){
        return new Reference(key, components);
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
        protected final String input;
        protected final Key key;
        protected final DataComponentAccessor components;
        public Reference(String input, DataComponentAccessor components) {
            this.input = input;
            Key key;
            try{
                key = Crux.key(input);
            }catch (RuntimeException e){
                key = null;
            }
            this.key = key;
            this.components = components;
        }

        @Override
        public void setBlock(@NotNull World world, @NotNull CruxPosition position, boolean applyPhysics) {

            if(key != null){
                CruxBlockWrapper wrapper = Crux.handlers().block().getBlockWrapper(key);
                Objects.requireNonNull(wrapper, "Block wrapper " + key + " not found!");
                wrapper.setBlock(world, position, applyPhysics);
            }else{
                BlockData data = Crux.getServer().createBlockData(input);
                Block b = position.getBlock(world);
                b.setType(data.getMaterial(), false);
                b.setBlockData(data);
            }

            if(components == null || components.isEmpty()) return;
            Block block = position.getBlock(world);
            if(block.isEmpty()) return;

            BlockState state = block.getState();
            PersistHolderComponentHandler handler = PersistentDataWrappers.wrapBlockState(state);
            for (TypedDataComponent<?> typed : components) {
                handler.set(typed);
            }
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
