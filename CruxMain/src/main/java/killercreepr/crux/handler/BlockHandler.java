package killercreepr.crux.handler;

import killercreepr.crux.block.CruxBlockWrapper;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BlockHandler {
    default @NotNull Block setType(@NotNull Block b, @NotNull Material m){
        return setType(b, m, true, true);
    }

    @NotNull Block setType(@NotNull Block b, @NotNull Material m, boolean applyPhysics, boolean removeTags);

    @Nullable
    CruxBlockWrapper getBlock(@NotNull Key key);

    class Dummy implements BlockHandler {

        @Override
        public @NotNull Block setType(@NotNull Block b, @NotNull Material m, boolean applyPhysics, boolean removeTags) {
            b.setType(m);
            return b;
        }

        @Override
        public @Nullable CruxBlockWrapper getBlock(@NotNull Key key) {
            Material material = Registry.MATERIAL.get(key);
            if(material==null) return null;
            return new CruxBlockWrapper.Vanilla(material);
        }
    }
}
