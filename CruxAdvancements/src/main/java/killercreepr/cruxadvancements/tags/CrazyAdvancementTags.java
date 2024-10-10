package killercreepr.cruxadvancements.tags;

import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.container.TagContainer;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.HookedObjectContainer;
import killercreepr.crux.tags.hook.ObjectTag;
import killercreepr.crux.tags.hook.impl.StringHookedObjectTag;
import killercreepr.crux.tags.hook.impl.StringListHookedObjectTag;
import killercreepr.crux.tags.hook.prefix.HookedPrefixBuilder;
import killercreepr.crux.tags.resolver.StringResolver;
import killercreepr.crux.tags.resolver.Tag;
import killercreepr.cruxadvancements.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.advancement.objective.AdvancementObjective;
import killercreepr.cruxadvancements.advancement.objective.NumberObjective;
import killercreepr.cruxadvancements.advancement.objective.progress.NumberObjectiveProgress;
import killercreepr.cruxadvancements.advancement.objective.progress.ObjectiveProgress;
import killercreepr.cruxadvancements.advancement.objective.progress.ObjectiveProgression;
import killercreepr.cruxadvancements.advancement.progression.CruxAdvancementProgress;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public class CrazyAdvancementTags implements ObjectTag<ObjectiveAdvancement> {
    @Override
    public @NotNull Class<ObjectiveAdvancement> getObjectType() {
        return ObjectiveAdvancement.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("advancement_");
    }

    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull ObjectiveAdvancement object, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("key", (args, ctx) -> object.key().asString()))
            .add(Tag.string("parent", (args, ctx) -> object.parent() + ""))
            .add(Tag.string("is_granted", ((args, ctx) -> {
                UUID uuid = UUID.fromString(ctx.deserializeString(args.get(0)));
                CruxAdvancementProgress progress = object.getProgressIfPresent(uuid);
                return (progress != null && progress.isDone()) + "";
            })))
            .add(Tag.string("objective_progress_percent", (args, ctx) ->{
                UUID uuid = UUID.fromString(ctx.deserializeString(args.get(0)));
                ObjectiveProgression progression = object.getObjectiveProgressIfPresent(uuid);
                if(progression == null) return "0";
                int maxProgress = 0;
                int currentProgress = 0;
                for(AdvancementObjective obj : object.getObjectives().values()){
                    if(obj instanceof NumberObjective num){
                        maxProgress += num.getMaxProgress();
                    }

                    ObjectiveProgress progress = progression.getProgressIfPresent(obj.getCriterion());
                    if(progress == null) continue;
                    if(progress instanceof NumberObjectiveProgress p){
                        currentProgress += p.getProgress();
                    }
                }
                /*for(Map.Entry<String, ObjectiveProgress> entry : progression.getProgressMap().entrySet()){
                    ObjectiveProgress progress = entry.getValue();
                    String criteria = entry.getKey();
                    if(progress instanceof NumberObjectiveProgress p){
                        currentProgress += p.getProgress();
                    }
                    if(object.getObjective(criteria) instanceof NumberObjective num){
                        maxProgress += num.getMaxProgress();
                    }
                }*/
                float x = (float) currentProgress / (float) maxProgress;
                return x + "";
            }))
            .add(Tag.string("objective_progress", (args, ctx) ->{
                UUID uuid = UUID.fromString(ctx.deserializeString(args.get(0)));
                ObjectiveProgression progression = object.getObjectiveProgressIfPresent(uuid);
                if(progression == null) return "0";
                int currentProgress = 0;
                for(Map.Entry<String, ObjectiveProgress> entry : progression.getProgressMap().entrySet()){
                    ObjectiveProgress progress = entry.getValue();
                    if(progress instanceof NumberObjectiveProgress p){
                        currentProgress += p.getProgress();
                    }
                }
                return currentProgress + "";
            }))
            .add(Tag.string("objective_progress_max", (args, ctx) ->{
                UUID uuid = UUID.fromString(ctx.deserializeString(args.get(0)));
                ObjectiveProgression progression = object.getObjectiveProgressIfPresent(uuid);
                if(progression == null) return "0";
                int maxProgress = 0;
                for(Map.Entry<String, ObjectiveProgress> entry : progression.getProgressMap().entrySet()){
                    String criteria = entry.getKey();
                    if(object.getObjective(criteria) instanceof NumberObjective num){
                        maxProgress += num.getMaxProgress();
                    }
                }
                return maxProgress + "";
            }))
            ;

    }

    @Override
    public @Nullable HookedObjectContainer<StringListHookedObjectTag<?>> hookStringLists(@NotNull ObjectiveAdvancement object, @NotNull TagParser tags) {
        return HookedObjectContainer.stringList()
            .addAll(tags.hookStringLists(object.getIcon(), HookedPrefixBuilder.overwrite(
                FormatPrefix.simple("advancement_icon/")
            )))
            ;
    }

    @Override
    public @Nullable HookedObjectContainer<StringHookedObjectTag<?>> hookStrings(@NotNull ObjectiveAdvancement object, @NotNull TagParser tags) {
        return HookedObjectContainer.string()
            .addAll(tags.hookStrings(object.getIcon(), HookedPrefixBuilder.overwrite(
                FormatPrefix.simple("advancement_icon/")
            )))
            ;
    }
}
