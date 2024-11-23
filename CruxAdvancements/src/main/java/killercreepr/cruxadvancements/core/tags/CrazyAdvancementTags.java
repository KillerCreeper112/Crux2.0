package killercreepr.cruxadvancements.core.tags;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.HookedObjectContainer;
import killercreepr.crux.api.text.hook.HookedPrefixBuilder;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringListResolver;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.hook.StringHookedObjectTag;
import killercreepr.crux.core.text.hook.StringListHookedObjectTag;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.cruxadvancements.api.advancement.ObjectiveAdvancement;
import killercreepr.cruxadvancements.api.advancement.icon.CriterionDisplay;
import killercreepr.cruxadvancements.api.advancement.objective.AdvancementObjective;
import killercreepr.cruxadvancements.api.advancement.objective.progress.ObjectiveProgress;
import killercreepr.cruxadvancements.api.advancement.objective.progress.ObjectiveProgression;
import killercreepr.cruxadvancements.api.advancement.progress.CruxAdvancementProgress;
import killercreepr.cruxadvancements.core.advancement.objective.NumberObjective;
import killercreepr.cruxadvancements.core.advancement.objective.progress.NumberObjectiveProgress;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
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
    public @Nullable TagContainer<StringListResolver> requestStringLists(@NotNull ObjectiveAdvancement object, @NotNull TagParser tags) {
        return TagContainer.stringList(tags)
            .add(Tag.stringList("objective_progress", (args, ctx) ->{
                UUID uuid = UUID.fromString(ctx.deserializeString(args.get(0)));
                ObjectiveProgression progression = object.getObjectiveProgressIfPresent(uuid);
                CriterionDisplay display = object.getIcon().getCriterionDisplay();
                List<String> list = new ArrayList<>();
                list.add(ctx.deserializeString(args.get(1)));
                if(display == null){
                    return list;
                }
                list.add("");
                display.getFormat().forEach((criterion, format) ->{
                    AdvancementObjective obj = object.getObjective(criterion);
                    if(!(obj instanceof NumberObjective num)) return;
                    int max = num.getMaxProgress();
                    ObjectiveProgress progress = progression == null ? null : progression.getProgressIfPresent(criterion);
                    int progressNum = progress == null ? 0 : progress.toType(NumberObjectiveProgress.class).getProgress();
                    var tagContainer = TagContainer.string(tags)
                        .add(Tag.parsed("max", max+""))
                        .add(Tag.parsed("progress", progressNum + ""));

                    list.add(ctx.deserializeString(args.get(2), tagContainer.add(
                        Tag.parsed("format", ctx.deserializeString(format, tagContainer))
                    )));
                });
                return list;
            }))
            ;
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
        var hooks = HookedObjectContainer.stringList()
            .addAll(tags.hookStringLists(object.getIcon(), HookedPrefixBuilder.overwrite(
                FormatPrefix.simple("advancement_icon/")
            )))
            ;
        return hooks;
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
