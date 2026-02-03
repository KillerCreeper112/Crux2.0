package killercreepr.crux.core.block.predicate;

import killercreepr.crux.api.block.CruxedBlock;
import killercreepr.crux.api.block.predicate.BlockPredicate;
import org.jetbrains.annotations.NotNull;

public class BlockPositionPredicate implements BlockPredicate {
    public final int x;
    public final int y;
    public final int z;

  public BlockPositionPredicate(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override
    public boolean test(@NotNull CruxedBlock block) {
        return block.getX() == x && block.getY() == y && block.getZ() == z;
    }
}
