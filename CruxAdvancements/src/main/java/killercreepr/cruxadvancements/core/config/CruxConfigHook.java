package killercreepr.cruxadvancements.core.config;

import killercreepr.crux.core.Crux;
import killercreepr.cruxadvancements.api.advancement.criteria.CruxCriteria;
import killercreepr.cruxadvancements.api.advancement.flag.CruxAdvancementFlag;
import killercreepr.cruxadvancements.api.advancement.icon.CriterionDisplay;
import killercreepr.cruxadvancements.api.advancement.objective.AdvancementObjective;
import killercreepr.cruxadvancements.api.advancement.objective.progress.ObjectiveProgress;
import killercreepr.cruxadvancements.api.advancement.reward.CruxAdvanceReward;
import killercreepr.cruxadvancements.core.advancement.objective.ObjectiveCommonData;
import killercreepr.cruxadvancements.core.advancement.objective.progress.NumberObjectiveProgress;
import killercreepr.cruxadvancements.core.advancement.objective.progress.SimpleObjectiveProgression;
import killercreepr.cruxadvancements.core.advancement.objective.standard.*;
import killercreepr.cruxadvancements.core.advancement.progress.ListAdvancementProgress;
import killercreepr.cruxadvancements.core.advancement.progress.NumberAdvancementProgress;
import killercreepr.cruxadvancements.core.advancement.progress.SimpleCriterionProgress;
import killercreepr.cruxadvancements.core.config.handler.*;
import killercreepr.cruxadvancements.core.data.TrackedAdvancement;
import killercreepr.cruxconfig.config.bukkit.handler.impl.FileGenericEnum;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxConfigHook {
    private static final FileAdvancementObjective fileAdvancementObjective = CruxAdvanceCfgData.fileAdvancementObjective();
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
        fileAdvancementObjective.registerCustomHandler(new FileSimpleAdvanceObjective<>(Crux.key("break_block")) {

            @Override
            public @Nullable BreakBlockObjective deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull ObjectiveCommonData data) {
                Integer maxProgress = e.getObject(Integer.class, "amount");
                if(maxProgress==null) maxProgress = 1;
                return new BreakBlockObjective(data, maxProgress);
            }
        });

        fileAdvancementObjective.registerCustomHandler(new FileSimpleAdvanceObjective<>(Crux.key("break_block_drop")) {
            @Override
            public @Nullable BreakBlockDropObjective deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull ObjectiveCommonData data) {
                Integer maxProgress = e.getObject(Integer.class, "amount");
                if(maxProgress==null) maxProgress = 1;
                return new BreakBlockDropObjective(data, maxProgress);
            }
        });

        fileAdvancementObjective.registerCustomHandler(new FileSimpleAdvanceObjective<>(Crux.key("place_block")) {

            @Override
            public @Nullable PlaceBlockObjective deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull ObjectiveCommonData data) {
                Integer maxProgress = e.getObject(Integer.class, "amount");
                if(maxProgress==null) maxProgress = 1;
                return new PlaceBlockObjective(data, maxProgress);
            }
        });

        fileAdvancementObjective.registerCustomHandler(new FileSimpleAdvanceObjective<>(Crux.key("kill_entity")) {

            @Override
            public @Nullable KillEntityObjective deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull ObjectiveCommonData data) {
                Integer maxProgress = e.getObject(Integer.class, "amount");
                if(maxProgress==null) maxProgress = 1;
                return new KillEntityObjective(data, maxProgress);
            }
        });

        fileAdvancementObjective.registerCustomHandler(new FileSimpleAdvanceObjective<>(Crux.key("fish")) {

            @Override
            public @Nullable FishObjective deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull ObjectiveCommonData data) {
                Integer maxProgress = e.getObject(Integer.class, "amount");
                if(maxProgress==null) maxProgress = 1;
                return new FishObjective(data, maxProgress);
            }
        });

        fileAdvancementObjective.registerCustomHandler(new FileSimpleAdvanceObjective<>(Crux.key("catch_fish")) {

            @Override
            public @Nullable CatchFishObjective deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull ObjectiveCommonData data) {
                Integer maxProgress = e.getObject(Integer.class, "amount");
                if(maxProgress==null) maxProgress = 1;
                return new CatchFishObjective(data, maxProgress);
            }
        });

        fileAdvancementObjective.registerCustomHandler(new FileSimpleAdvanceObjective<>(Crux.key("consume_item")) {

            @Override
            public @Nullable ConsumeItemObjective deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull ObjectiveCommonData data) {
                Integer maxProgress = e.getObject(Integer.class, "amount");
                if(maxProgress==null) maxProgress = 1;
                return new ConsumeItemObjective(data, maxProgress);
            }
        });

        fileAdvancementObjective.registerCustomHandler(new FileSimpleAdvanceObjective<>(Crux.key("travel_to_world")){

            @Override
            public @Nullable TravelToWorldObjective deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull ObjectiveCommonData data) {
                Integer maxProgress = e.getObject(Integer.class, "amount");
                if(maxProgress==null) maxProgress = 1;
                return new TravelToWorldObjective(data, maxProgress);
            }
        });

        fileAdvancementObjective.registerCustomHandler(new FileSimpleAdvanceObjective<>(Crux.key("breed_entity")) {

            @Override
            public @Nullable BreedEntityObjective deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull ObjectiveCommonData data) {
                Integer maxProgress = e.getObject(Integer.class, "amount");
                if(maxProgress==null) maxProgress = 1;
                return new BreedEntityObjective(data, maxProgress);
            }
        });

        fileAdvancementObjective.registerCustomHandler(new FileSimpleAdvanceObjective<>(Crux.key("resurrect")) {

            @Override
            public @Nullable ResurrectObjective deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull ObjectiveCommonData data) {
                Integer maxProgress = e.getObject(Integer.class, "amount");
                if(maxProgress==null) maxProgress = 1;
                return new ResurrectObjective(data, maxProgress);
            }
        });

        fileAdvancementObjective.registerCustomHandler(new FileSimpleAdvanceObjective<>(Crux.key("shear_block")) {

            @Override
            public @Nullable AdvancementObjective deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull ObjectiveCommonData data) {
                Integer maxProgress = e.getObject(Integer.class, "amount");
                if(maxProgress==null) maxProgress = 1;
                return new ShearBlockObjective(data, maxProgress);
            }
        });

        fileAdvancementObjective.registerCustomHandler(new FileSimpleAdvanceObjective<>(Crux.key("shear_entity")) {

            @Override
            public @Nullable AdvancementObjective deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull ObjectiveCommonData data) {
                Integer maxProgress = e.getObject(Integer.class, "amount");
                if(maxProgress==null) maxProgress = 1;
                return new ShearEntityObjective(data, maxProgress);
            }
        });

        fileAdvancementObjective.registerCustomHandler(new FileSimpleAdvanceObjective<>(Crux.key("tame_entity")) {

            @Override
            public @Nullable AdvancementObjective deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull ObjectiveCommonData data) {
                Integer maxProgress = e.getObject(Integer.class, "amount");
                if(maxProgress==null) maxProgress = 1;
                return new TameEntityObjective(data, maxProgress);
            }
        });

        fileAdvancementObjective.registerCustomHandler(new FileSimpleAdvanceObjective<>(Crux.key("pickup_item")){

            @Override
            public @Nullable AdvancementObjective deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull ObjectiveCommonData data) {
                Integer maxProgress = e.getObject(Integer.class, "amount");
                if(maxProgress==null) maxProgress = 1;
                return new PickupItemObjective(data, maxProgress);
            }
        });

        fileAdvancementObjective.registerCustomHandler(new FileSimpleAdvanceObjective<>(Crux.key("craft_item")) {

            @Override
            public @Nullable AdvancementObjective deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull ObjectiveCommonData data) {
                Integer maxProgress = e.getObject(Integer.class, "amount");
                if(maxProgress==null) maxProgress = 1;
                return new CraftItemObjective(data, maxProgress);
            }
        });

        fileAdvancementObjective.registerCustomHandler(new FileSimpleAdvanceObjective<>(Crux.key("take_damage")) {

            @Override
            public @Nullable AdvancementObjective deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull ObjectiveCommonData data) {
                Integer maxProgress = e.getObject(Integer.class, "amount");
                if(maxProgress==null) maxProgress = 1;
                return new TakeDamageObjective(data, maxProgress);
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
        //registry.registerFileHandler(CruxAdvancementProgress.class, CRUX_ADVANCEMENT_PROGRESS);
        //register the inheritors objects so the config registry doesn't pick a random handler
        registry.registerFileHandler(ListAdvancementProgress.class, CRUX_ADVANCEMENT_PROGRESS);
        registry.registerFileHandler(NumberAdvancementProgress.class, CRUX_ADVANCEMENT_PROGRESS);
        registry.registerFileHandler(CruxAdvanceReward.class, new FileCruxAdvanceReward());

        registry.registerFileHandler(ObjectiveProgress.class, OBJECTIVE_PROGRESS);

        registry.registerFileHandler(SimpleObjectiveProgression.class, SIMPLE_OBJECTIVE_PROGRESSION);

        registry.registerFileHandler(TrackedAdvancement.class, new FileTrackedAdvancement());

        registry.registerFileHandler(CriterionDisplay.class, new FileCriterionDisplay());
        registry.registerFileHandler(CruxAdvancementFlag.class, new FileGenericEnum<>(CruxAdvancementFlag.class));
    }
}
