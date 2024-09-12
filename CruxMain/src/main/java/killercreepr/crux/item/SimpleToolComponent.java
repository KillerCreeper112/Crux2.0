package killercreepr.crux.item;

import killercreepr.crux.block.CruxedBlock;
import killercreepr.crux.block.predicate.BlockPredicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleToolComponent implements ToolComponent{
    protected final @NotNull BlockPredicate predicate;
    protected final @NotNull Result result;
    public SimpleToolComponent(@NotNull BlockPredicate predicate, boolean canHarvest, float speed) {
        this.predicate = predicate;
        this.result = new Result(canHarvest, speed);
    }

    @Override
    public @Nullable ToolComponent.Result test(@NotNull CruxedBlock block) {
        if(!predicate.test(block)) return null;
        return result;
    }

    public static class Result implements ToolComponent.Result{
        protected final boolean canHarvest;
        protected final float speed;
        public Result(boolean canHarvest, float speed) {
            this.canHarvest = canHarvest;
            this.speed = speed;
        }

        @Override
        public boolean canHarvest() {
            return canHarvest;
        }

        @Override
        public float getSpeed() {
            return speed;
        }
    }
}
