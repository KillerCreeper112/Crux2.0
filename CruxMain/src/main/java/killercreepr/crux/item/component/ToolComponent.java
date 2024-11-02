package killercreepr.crux.item.component;

import killercreepr.crux.block.CruxedBlock;
import killercreepr.crux.block.predicate.BlockPredicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ToolComponent {
    float getDefaultMiningSpeed();

    @Nullable Result test(@NotNull CruxedBlock block);
    interface Result{
        boolean canHarvest();
        float getSpeed();
    }

    interface Rule{
        @Nullable Float getSpeed();
        boolean isCorrectToolForDrops();
        boolean test(@NotNull CruxedBlock block);
    }

    class Simple implements ToolComponent {
        protected final float defaultMiningSpeed;
        protected final @Nullable List<ToolComponent.Rule> rules;

        public Simple(float defaultMiningSpeed, @Nullable List<ToolComponent.Rule> rules) {
            this.defaultMiningSpeed = defaultMiningSpeed;
            this.rules = rules;
        }

        @Override
        public float getDefaultMiningSpeed() {
            return defaultMiningSpeed;
        }

        @Override
        public @Nullable ToolComponent.Result test(@NotNull CruxedBlock block) {
            if(rules == null || rules.isEmpty()) return new Result(false, defaultMiningSpeed);
            for(ToolComponent.Rule rule : rules){
                Float speed = rule.getSpeed();
                if(rule.test(block)) return new Result(
                    rule.isCorrectToolForDrops(), speed == null ? defaultMiningSpeed : speed
                );
            }
            return new Result(false, defaultMiningSpeed);
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
        public static class Rule implements ToolComponent.Rule{
            protected final @Nullable BlockPredicate predicate;
            protected final @Nullable Float speed;
            protected final @Nullable Boolean isCorrectToolForDrops;

            public Rule(@Nullable BlockPredicate predicate, @Nullable Float speed, @Nullable Boolean isCorrectToolForDrops) {
                this.predicate = predicate;
                this.speed = speed;
                this.isCorrectToolForDrops = isCorrectToolForDrops;
            }

            @Override
            public String toString() {
                return "SimpleToolComponentRule{predicate=" + predicate + ", speed=" + speed + ", isCorrectToolForDrops=" + isCorrectToolForDrops + "}";
            }

            @Override
            public @Nullable Float getSpeed() {
                return speed;
            }

            @Override
            public boolean isCorrectToolForDrops() {
                return isCorrectToolForDrops != null && isCorrectToolForDrops;
            }

            public boolean test(@NotNull CruxedBlock block){
                return predicate == null || predicate.test(block);
            }
        }
    }
}
