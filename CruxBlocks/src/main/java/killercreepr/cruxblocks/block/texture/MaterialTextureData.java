package killercreepr.cruxblocks.block.texture;

import killercreepr.crux.Crux;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.LimitedRegion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class MaterialTextureData implements TextureData {
    public static @NotNull Builder builder(){
        return new Builder();
    }

    protected final @NotNull Material material;
    public MaterialTextureData(@NotNull Material material) {
        this.material = material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof MaterialTextureData that)) return false;
        // Compare the data members and return accordingly
        return that.getMaterial() == material;
    }

    @Override
    public int hashCode() {
        // Generate a hash code based on note, instrument, and powered
        return Objects.hash(material);
    }

    @Override
    public String toString() {
        return "MaterialTextureData{material=" + material + "}";
    }

    public @NotNull Material getMaterial() {
        return material;
    }

    @Override
    public boolean compareTexture(@Nullable Block b) {
        return b != null && compareTexture(b.getBlockData());
    }

    @Override
    public boolean compareTexture(@Nullable BlockData data) {
        return data != null && data.getMaterial() == material;
    }

    @Override
    public boolean compareTexture(@Nullable TextureData data) {
        return data instanceof MaterialTextureData t && t.getMaterial() == material;
    }

    @Override
    public @NotNull BlockData applyToBlockData(@NotNull BlockData data) {
        return data;
    }

    @Override
    public void setBlock(@NotNull Block block, boolean applyPhysics, boolean removeTags, Consumer<Block> consumer) {
        Crux.handlers().block().setType(block, material, applyPhysics, removeTags);
        if(consumer != null) consumer.accept(block);
        applyToBlock(block, applyPhysics);
    }

    @Override
    public void setBlock(@NotNull LimitedRegion region, int x, int y, int z) {
        region.setType(x, y, z, material);
    }

    public static final class Builder {
        private Material material;

        public Builder() {
        }

        public Builder(Material material) {
            this.material = material;
        }

        public Builder material(@NotNull Material material) {
            this.material = material;
            return this;
        }

        public MaterialTextureData build() {
            return new MaterialTextureData(material);
        }
    }
}
