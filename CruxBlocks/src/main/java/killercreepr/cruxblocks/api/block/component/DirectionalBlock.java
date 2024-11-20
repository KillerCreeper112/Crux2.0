package killercreepr.cruxblocks.api.block.component;

import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;

public interface DirectionalBlock {
    @NotNull BlockFace getDirection();

    class Simple implements DirectionalBlock{
        protected final @NotNull BlockFace direction;

        public Simple(@NotNull BlockFace direction) {
            this.direction = direction;
        }

        @Override
        public @NotNull BlockFace getDirection() {
            return direction;
        }
    }
}
