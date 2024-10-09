package killercreepr.crux.handler;

import killercreepr.crux.block.BukkitCruxedBlock;
import killercreepr.crux.block.CruxBlockWrapper;
import killercreepr.crux.block.CruxedBlock;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BlockHandler {
    default @NotNull Block setType(@NotNull Block b, @NotNull Material m){
        return setType(b, m, true, true);
    }

    @NotNull Block setType(@NotNull Block b, @NotNull Material m, boolean applyPhysics, boolean removeTags);
    @NotNull Key getType(@NotNull Block block);

    @Nullable
    CruxBlockWrapper getBlockWrapper(@NotNull Key key);

    @Nullable
    CruxedBlock getBlock(@NotNull Block block);
    @NotNull Key getType(@NotNull BlockData block);

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
        public @NotNull Key getType(@NotNull BlockData block) {
            return block.getMaterial().key();
        }
    }
}
