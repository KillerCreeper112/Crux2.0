package killercreepr.crux.item;

import killercreepr.crux.block.CruxedBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ToolComponent {
    @Nullable Result test(@NotNull CruxedBlock block);
    interface Result{
        boolean canHarvest();
        float getSpeed();
    }
}
