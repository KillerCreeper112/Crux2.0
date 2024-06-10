package killercreepr.cruxblocks.block.texture;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class VanillaTextureData implements TextureData {
    public static @NotNull Builder builder(){
        return new Builder();
    }

    protected final @NotNull Material material;
    public VanillaTextureData(@NotNull Material material) {
        this.material = material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof VanillaTextureData that)) return false;
        // Compare the data members and return accordingly
        return that.getMaterial() == material;
    }

    @Override
    public int hashCode() {
        // Generate a hash code based on note, instrument, and powered
        return Objects.hash(material);
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
        return data instanceof VanillaTextureData t && t.getMaterial() == material;
    }

    @Override
    public @NotNull BlockData applyToBlockData(@NotNull BlockData data) {
        return data;
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

        public VanillaTextureData build() {
            return new VanillaTextureData(material);
        }
    }
}
