package killercreepr.cruxblocks.core.block.texture;

import killercreepr.crux.core.Crux;
import killercreepr.cruxblocks.api.block.texture.TextureData;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.BlockType;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.LimitedRegion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class NoteTextureData implements TextureData {
    public static @NotNull Builder builder(){
        return new Builder();
    }

    protected final @NotNull Note note;
    protected final @NotNull Instrument instrument;
    protected final boolean powered;

    public NoteTextureData(@NotNull Note note, @NotNull Instrument instrument, boolean powered) {
        this.note = note;
        this.instrument = instrument;
        this.powered = powered;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof NoteTextureData that)) return false;
        // Compare the data members and return accordingly
        return powered == that.powered &&
            note.equals(that.note) &&
            instrument.equals(that.instrument);
    }

    @Override
    public int hashCode() {
        // Generate a hash code based on note, instrument, and powered
        return Objects.hash(note, instrument, powered);
    }

    @Override
    public String toString() {
        return "NoteTextureData{note=" + note + ", instrument=" + instrument + ", powered=" + powered + "}";
    }

    public @NotNull Note getNote() {
        return note;
    }

    public @NotNull Instrument getInstrument() {
        return instrument;
    }

    public boolean isPowered() {
        return powered;
    }

    @Override
    public boolean compareTexture(@Nullable Block b) {
        if(b == null || b.isEmpty()) return false;
        return compareTexture(b.getBlockData());
    }

    @Override
    public boolean compareTexture(@Nullable BlockData b) {
        if(b == null) return false;
        return b instanceof NoteBlock n && n.getInstrument() == instrument && n.getNote().equals(note) && n.isPowered() == powered;
    }

    @Override
    public boolean compareTexture(@Nullable TextureData data) {
        return data instanceof NoteTextureData n && n.isPowered() == powered && n.getInstrument() == instrument && n.getNote().equals(note);
    }

    @Override
    public @NotNull BlockData applyToBlockData(@NotNull BlockData data) {
        if(!(data instanceof NoteBlock noteData)) return data;
        noteData.setNote(getNote());
        noteData.setInstrument(getInstrument());
        noteData.setPowered(isPowered());
        return noteData;
    }

    /*@Override
    public void setBlock(@NotNull Block block, boolean applyPhysics) {
        Crux.handlers().block().setType(block, Material.NOTE_BLOCK, applyPhysics, true);
        applyToBlock(block, applyPhysics);
    }*/

    @Override
    public void setBlock(@NotNull Block block, boolean applyPhysics, boolean removeTags, Consumer<Block> consumer) {
        Crux.handlers().block().setType(block, Material.NOTE_BLOCK, applyPhysics, removeTags);
        if(consumer != null) consumer.accept(block);
        applyToBlock(block, applyPhysics);
    }

    public void setBlock(@NotNull LimitedRegion region, int x, int y, int z){
        region.setType(x, y, z, Material.NOTE_BLOCK);
        region.setBlockData(x, y, z, applyToBlockData(region.getBlockData(x, y, z)));
    }

    @Override
    public void setBlock(ChunkGenerator.@NotNull ChunkData chunkData, int x, int y, int z) {
        chunkData.setBlock(x,y,z, applyToBlockData(BlockType.NOTE_BLOCK.createBlockData()));
    }

    public static final class Builder {
        private Note note;
        private Instrument instrument;
        private boolean powered;

        public Builder() {
        }

        public Builder(Note note, Instrument instrument) {
            this.note = note;
            this.instrument = instrument;
        }

        public Builder note(@NotNull Note note) {
            this.note = note;
            return this;
        }

        public Builder instrument(@NotNull Instrument instrument) {
            this.instrument = instrument;
            return this;
        }

        public Builder powered(boolean powered) {
            this.powered = powered;
            return this;
        }

        public NoteTextureData build() {
            Objects.requireNonNull(note);
            Objects.requireNonNull(instrument);
            return new NoteTextureData(note, instrument, powered);
        }
    }
}
