package killercreepr.crux.api.handler;

import killercreepr.crux.api.block.CruxBlockWrapper;
import killercreepr.crux.api.block.CruxedBlock;
import killercreepr.crux.paper.block.BukkitCruxedBlock;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BlockHandler {
    default @NotNull Block setType(@NotNull Block b, @NotNull Material m){
        return setType(b, m, true, true);
    }

    @NotNull Block setType(@NotNull Block b, @NotNull Material m, boolean applyPhysics, boolean removeTags);
    @NotNull Key getType(@NotNull Block block);
    void removeTags(@NotNull Block b);

    @Nullable
    CruxBlockWrapper getBlockWrapper(@NotNull Key key);

    @Nullable
    CruxedBlock getBlock(@NotNull Block block);
    @NotNull Key getType(@NotNull Block block, @NotNull BlockData data);
    @NotNull Key getType(@NotNull BlockState state);

    class Dummy implements BlockHandler {

        @Override
        public @NotNull Block setType(@NotNull Block b, @NotNull Material m, boolean applyPhysics, boolean removeTags) {
            b.setType(m);
            return b;
        }

        @Override
        public @NotNull Key getType(@NotNull Block block) {
            return block.getType().key();
        }

        @Override
        public void removeTags(@NotNull Block b) {

        }

        @Override
        public @Nullable CruxBlockWrapper getBlockWrapper(@NotNull Key key) {
            Material material = Registry.MATERIAL.get(key);
            if(material==null) return null;
            return new CruxBlockWrapper.Vanilla(material);
        }

        @Override
        public @Nullable CruxedBlock getBlock(@NotNull Block block) {
            return new BukkitCruxedBlock(block);
        }

        @Override
        public @NotNull Key getType(@NotNull Block block, @NotNull BlockData data) {
            return data.getMaterial().key();
        }

        @Override
        public @NotNull Key getType(@NotNull BlockState state) {
            return state.getType().key();
        }
    }
}
