package killercreepr.crux.api.item.component;

import killercreepr.crux.api.block.CruxedBlock;
import killercreepr.crux.api.block.predicate.BlockPredicate;
import killercreepr.crux.api.component.parser.persistent.ComponentInputField;
import killercreepr.crux.api.component.parser.persistent.PersistentTextParser;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.component.parser.type.ComponentInputParsers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ToolComponent {
    PersistentTextParser<ToolComponent> INPUT_PARSER = PersistentTextParser.mapBuilder(ToolComponent.class)
        .key("tool")
        .field("default_mining_speed", ComponentInputField.createFloat(ToolComponent::getDefaultMiningSpeed))
        .field("rules", ComponentInputField.createList(Rule.INPUT_PARSER, ToolComponent::getRules))
        .apply(ctx -> new Simple(ctx.decodeOptional("default_mining_speed", 1f), ctx.decodeOptional("rules")));

    float getDefaultMiningSpeed();

    @Nullable Result test(@NotNull CruxedBlock block);
    @Nullable
    List<Rule> getRules();
    interface Result{
        boolean canHarvest();
        float getSpeed();
    }

    interface Rule{

        PersistentTextParser<Rule> INPUT_PARSER = PersistentTextParser.mapBuilder(Rule.class)
            .key("tool_rule")
            .field("blocks", ComponentInputField.create(ComponentInputParsers.blockPredicate(Crux.key("blocks")), Rule::getBlocks))
            .field("speed", ComponentInputField.createFloat(Rule::getSpeed))
            .field("correct_for_drops", ComponentInputField.createBool(Rule::isCorrectToolForDrops))
            .apply(ctx -> new Simple.Rule(ctx.decode("blocks"), ctx.decode("speed"), ctx.decode("correct_for_drops")));

        @Nullable Float getSpeed();
        boolean isCorrectToolForDrops();
        boolean test(@NotNull CruxedBlock block);
        @Nullable BlockPredicate getBlocks();
    }

    class Simple implements ToolComponent {
        protected final float defaultMiningSpeed;
        protected final @Nullable List<ToolComponent.Rule> rules;

        public Simple(float defaultMiningSpeed, @Nullable List<ToolComponent.Rule> rules) {
            this.defaultMiningSpeed = defaultMiningSpeed;
            this.rules = rules;
        }

        @Override
        public @Nullable List<ToolComponent.Rule> getRules() {
            return rules;
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

            public @Nullable BlockPredicate getPredicate() {
                return predicate;
            }

            public @Nullable Boolean getCorrectToolForDrops() {
                return isCorrectToolForDrops;
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

            @Override
            public @Nullable BlockPredicate getBlocks() {
                return predicate;
            }
        }
    }
}
