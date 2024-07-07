package killercreepr.cruxblocks.block.texture;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Tripwire;
import org.bukkit.generator.LimitedRegion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

public class WireTextureData implements TextureData {
    public static @NotNull Builder builder(){
        return new Builder();
    }

    protected final boolean attached;
    protected final boolean disarmed;
    protected final boolean powered;
    protected final @Nullable Collection<BlockFace> faces;
    public WireTextureData(boolean attached, boolean disarmed, boolean powered, @Nullable Collection<BlockFace> faces) {
        this.attached = attached;
        this.disarmed = disarmed;
        this.powered = powered;
        this.faces = faces == null ? null : Collections.unmodifiableCollection(faces);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof WireTextureData that)) return false;
        return attached == that.attached &&
            disarmed == that.disarmed &&
            powered == that.powered &&
            Objects.equals(faces, that.faces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attached, disarmed, powered, faces);
    }

    public boolean isAttached() { return attached; }

    public boolean isDisarmed() {
        return disarmed;
    }

    public boolean isPowered() {
        return powered;
    }

    public @Nullable Collection<BlockFace> getFaces(){ return faces; }

    public boolean hasFace(BlockFace face){
        return faces != null && faces.contains(face);
    }

    @Override
    public boolean compareTexture(@Nullable Block b) {
        if(b == null || b.isEmpty()) return false;
        return compareTexture(b.getBlockData());
    }

    @Override
    public boolean compareTexture(@Nullable BlockData b) {
        if(!(b instanceof Tripwire n)) return false;
        if(n.isPowered() == powered && n.isDisarmed() == disarmed && n.isAttached() == attached){
            for(BlockFace f : n.getAllowedFaces()){
                if(n.hasFace(f) != hasFace(f)) return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean compareTexture(@Nullable TextureData data) {
        if(!(data instanceof WireTextureData n)) return false;
        if(n.isPowered() == powered && n.isDisarmed() == disarmed && n.isAttached() == attached){
            if(faces != null){
                for(BlockFace f : faces){
                    if(n.hasFace(f) != hasFace(f)) return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public @NotNull BlockData applyToBlockData(@NotNull BlockData data) {
        if(!(data instanceof Tripwire wire)) return data;
        for(BlockFace f : wire.getAllowedFaces()){
            wire.setFace(f, hasFace(f));
        }
        wire.setAttached(isAttached());
        wire.setDisarmed(isDisarmed());
        wire.setPowered(isPowered());
        return data;
    }

    @Override
    public void setBlock(@NotNull Block block, boolean applyPhysics) {
        block.setType(Material.TRIPWIRE, false);
        applyToBlock(block, applyPhysics);
    }

    @Override
    public void setBlock(@NotNull LimitedRegion region, int x, int y, int z) {
        region.setType(x, y, z, Material.TRIPWIRE);
        region.setBlockData(x, y, z, applyToBlockData(region.getBlockData(x, y, z)));
    }

    public static final class Builder {
        private boolean attached;
        private boolean disarmed;
        private boolean powered;
        private @Nullable Collection<BlockFace> faces;

        public Builder attached(boolean attached) {
            this.attached = attached;
            return this;
        }

        public Builder disarmed(boolean disarmed) {
            this.disarmed = disarmed;
            return this;
        }

        public Builder powered(boolean powered) {
            this.powered = powered;
            return this;
        }

        public Builder faces(@Nullable Collection<BlockFace> faces) {
            this.faces = faces;
            return this;
        }

        public Builder setFace(BlockFace face, boolean has){
            if(has){
                if(faces == null) faces = new HashSet<>();
                faces.add(face);
            } else{
                if(faces == null) return this;
                faces.remove(face);
            }
            return this;
        }

        public @NotNull WireTextureData build() {
            return new WireTextureData(attached, disarmed, powered, faces);
        }
    }
}
