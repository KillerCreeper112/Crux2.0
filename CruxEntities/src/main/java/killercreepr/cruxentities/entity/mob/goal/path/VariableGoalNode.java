package killercreepr.cruxentities.entity.mob.goal.path;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.data.holder.DataInfoHolder;
import killercreepr.cruxentities.api.entity.mob.goal.PathTargetMobGoal;
import killercreepr.cruxentities.api.entity.mob.goal.path.GoalNode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class VariableGoalNode implements GoalNode, DataInfoHolder {
    protected final Consumer<PathTargetMobGoal> onTick;
    protected final Consumer<PathTargetMobGoal> onStart;
    protected final Consumer<PathTargetMobGoal> onFinish;
    protected final DataExchange info;

    public VariableGoalNode(Consumer<PathTargetMobGoal> onTick, Consumer<PathTargetMobGoal> onStart, Consumer<PathTargetMobGoal> onFinish, DataExchange info) {
        this.onTick = onTick;
        this.onStart = onStart;
        this.onFinish = onFinish;
        this.info = info;
    }

    @Override
    public @NotNull DataExchange info() {
        return info;
    }

    @Override
    public void onTick(PathTargetMobGoal goal) {
        if(onTick != null) onTick.accept(goal);
    }

    @Override
    public void onStart(PathTargetMobGoal goal) {
        if(onStart != null) onStart.accept(goal);
    }

    @Override
    public void onFinish(PathTargetMobGoal goal) {
        if(onFinish != null) onFinish.accept(goal);
    }

    public static class SimpleBuilder implements GoalNode.Builder {
        private Consumer<PathTargetMobGoal> onTick;
        private Consumer<PathTargetMobGoal> onStart;
        private Consumer<PathTargetMobGoal> onFinish;
        private Function<Entity, Boolean> canMoveOn;
        private DataExchange info = DataExchange.empty();

        @Override
        public Builder onTick(Consumer<PathTargetMobGoal> onTick) {
            this.onTick = onTick;
            return this;
        }

        @Override
        public Builder onStart(Consumer<PathTargetMobGoal> onStart) {
            this.onStart = onStart;
            return this;
        }

        @Override
        public Builder onFinish(Consumer<PathTargetMobGoal> onFinish) {
            this.onFinish = onFinish;
            return this;
        }

        @Override
        public Builder info(DataExchange info) {
            this.info = info;
            return this;
        }

        public Builder canMoveOn(Function<Entity, Boolean> canMoveOn) {
            this.canMoveOn = canMoveOn;
            return this;
        }

        @Override
        public GoalNode buildDynamicDistance(@NotNull Holder<Location> locationHolder, double distance) {
            return new VariableDynamicDistanceGoalNode(onTick, onStart, onFinish, info, locationHolder, distance);
        }

        @Override
        public GoalNode buildDistance(double x, double y, double z, double distance) {
            return new VariableDistanceGoalNode(onTick, onStart, onFinish, info, x, y, z, distance);
        }

        @Override
        public GoalNode buildDistance(@NotNull Location loc, double distance) {
            return buildDistance(loc.getX(), loc.getY(), loc.getZ(), distance);
        }
    }

}
