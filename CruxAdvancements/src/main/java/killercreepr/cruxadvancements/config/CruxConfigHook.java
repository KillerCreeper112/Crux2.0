package killercreepr.cruxadvancements.config;

import killercreepr.crux.loot.conditions.LootCondition;
import killercreepr.cruxadvancements.advancement.criteria.CruxCriteria;
import killercreepr.cruxadvancements.advancement.objective.impl.*;
import killercreepr.cruxadvancements.advancement.objective.progress.NumberObjectiveProgress;
import killercreepr.cruxadvancements.advancement.objective.progress.ObjectiveProgress;
import killercreepr.cruxadvancements.advancement.objective.progress.SimpleObjectiveProgression;
import killercreepr.cruxadvancements.advancement.progression.CruxAdvancementProgress;
import killercreepr.cruxadvancements.advancement.progression.ListAdvancementProgress;
import killercreepr.cruxadvancements.advancement.progression.NumberAdvancementProgress;
import killercreepr.cruxadvancements.advancement.progression.SimpleCriterionProgress;
import killercreepr.cruxadvancements.advancement.reward.CruxAdvanceReward;
import killercreepr.cruxadvancements.config.handler.*;
import killercreepr.cruxadvancements.data.TrackedAdvancement;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxConfigHook {
    public static void load(){
        OBJECTIVE_PROGRESS.registerCustomHandler(NumberObjectiveProgress.class, new CustomFileObjectiveProgress<NumberObjectiveProgress>() {
            @Override
            public @NotNull String getType() {
                return "number";
            }

            @Override
            public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull NumberObjectiveProgress n) {
                return new FileObject()
                    .addProperty("type", "number")
                    .addProperty("progress", n.getProgress())
                    ;
            }

            @Override
            public @Nullable NumberObjectiveProgress deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                Integer progress = e.getObject(Integer.class, "progress");
                if(progress==null) return null;
                return new NumberObjectiveProgress(progress);
            }
        });
        FileAdvancementObjective.registerCustomHandler(new CustomFileAdvancementObjective<BreakBlockObjective>() {
            @Override
            public @NotNull String getType() {
                return "break_block";
            }

            @Override
            public @Nullable BreakBlockObjective deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String criterion, @Nullable LootCondition conditions) {
                Integer maxProgress = e.getObject(Integer.class, "amount");
                if(maxProgress==null) maxProgress = 1;
                return new BreakBlockObjective(criterion, conditions, maxProgress);
            }
        });

        FileAdvancementObjective.registerCustomHandler(new CustomFileAdvancementObjective<PlaceBlockObjective>() {
            @Override
            public @NotNull String getType() {
                return "place_block";
            }

            @Override
            public @Nullable PlaceBlockObjective deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String criterion, @Nullable LootCondition conditions) {
                Integer maxProgress = e.getObject(Integer.class, "amount");
                if(maxProgress==null) maxProgress = 1;
                return new PlaceBlockObjective(criterion, conditions, maxProgress);
            }
        });

        FileAdvancementObjective.registerCustomHandler(new CustomFileAdvancementObjective<KillEntityObjective>() {
            @Override
            public @NotNull String getType() {
                return "kill_entity";
            }

            @Override
            public @Nullable KillEntityObjective deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String criterion, @Nullable LootCondition conditions) {
                Integer maxProgress = e.getObject(Integer.class, "amount");
                if(maxProgress==null) maxProgress = 1;
                return new KillEntityObjective(criterion, conditions, maxProgress);
            }
        });

        FileAdvancementObjective.registerCustomHandler(new CustomFileAdvancementObjective<FishObjective>() {
            @Override
            public @NotNull String getType() {
                return "fish";
            }

            @Override
            public @Nullable FishObjective deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String criterion, @Nullable LootCondition conditions) {
                Integer maxProgress = e.getObject(Integer.class, "amount");
                if(maxProgress==null) maxProgress = 1;
                return new FishObjective(criterion, conditions, maxProgress);
            }
        });

        FileAdvancementObjective.registerCustomHandler(new CustomFileAdvancementObjective<CatchFishObjective>() {
            @Override
            public @NotNull String getType() {
                return "catch_fish";
            }

            @Override
            public @Nullable CatchFishObjective deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String criterion, @Nullable LootCondition conditions) {
                Integer maxProgress = e.getObject(Integer.class, "amount");
                if(maxProgress==null) maxProgress = 1;
                return new CatchFishObjective(criterion, conditions, maxProgress);
            }
        });
    }

    public static void registerHandlers(){
        CfgRegistries.FILE.forEach(CruxConfigHook::registerHandlers);
    }

    public static final FileCruxAdvancementProgress CRUX_ADVANCEMENT_PROGRESS = new FileCruxAdvancementProgress();
    public static final FileSimpleObjectiveProgression SIMPLE_OBJECTIVE_PROGRESSION = new FileSimpleObjectiveProgression();
    public static final FileObjectiveProgress OBJECTIVE_PROGRESS = new FileObjectiveProgress();
    public static void registerHandlers(@NotNull FileRegistry registry){
        registry.registerFileHandler(CruxCriteria.class, new FileCruxCriteria());
        registry.registerFileHandler(SimpleCriterionProgress.class, new FileSimpleCriterionProgress());
        registry.registerFileHandler(CruxAdvancementProgress.class, CRUX_ADVANCEMENT_PROGRESS);
        //register the inheritors objects so the config registry doesn't pick a random handler
        registry.registerFileHandler(ListAdvancementProgress.class, CRUX_ADVANCEMENT_PROGRESS);
        registry.registerFileHandler(NumberAdvancementProgress.class, CRUX_ADVANCEMENT_PROGRESS);
        registry.registerFileHandler(CruxAdvanceReward.class, new FileCruxAdvanceReward());

        registry.registerFileHandler(ObjectiveProgress.class, OBJECTIVE_PROGRESS);

        registry.registerFileHandler(SimpleObjectiveProgression.class, SIMPLE_OBJECTIVE_PROGRESSION);

        registry.registerFileHandler(TrackedAdvancement.class, new FileTrackedAdvancement());
    }
}
