package killercreepr.crux.handler;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public interface BlockHandler {
    default @NotNull Block setType(@NotNull Block b, @NotNull Material m){
        return setType(b, m, true, true);
    }

    @NotNull Block setType(@NotNull Block b, @NotNull Material m, boolean applyPhysics, boolean removeTags);

    class Dummy implements BlockHandler {

        @Override
        public @NotNull Block setType(@NotNull Block b, @NotNull Material m, boolean applyPhysics, boolean removeTags) {
            b.setType(m);
            return b;
        }
    }
}
