package killercreepr.cruxadvancements.config.handler;

import killercreepr.cruxadvancements.advancement.criteria.CruxCriteria;
import killercreepr.cruxadvancements.advancement.criteria.ListCriteria;
import killercreepr.cruxadvancements.advancement.criteria.NumberCriteria;
import killercreepr.cruxadvancements.advancement.progression.CruxAdvancementProgress;
import killercreepr.cruxadvancements.advancement.progression.ListAdvancementProgress;
import killercreepr.cruxadvancements.advancement.progression.NumberAdvancementProgress;
import killercreepr.cruxadvancements.advancement.progression.SimpleCriterionProgress;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public class FileCruxAdvancementProgress implements FileObjectHandler<CruxAdvancementProgress> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull CruxAdvancementProgress object) {
        FileRegistry registry = ctx.getRegistry();
        if(object instanceof ListAdvancementProgress a){
            FileObject o = new FileObject();
            Instant obtained = a.getObtained();
            if(obtained != null) o.add("obtained", registry.serializeToFile(obtained));
            FileObject progress = new FileObject();
            a.getProgressMap().forEach((name, prog) ->{
                progress.add(name, registry.serializeToFile(prog));
            });
            o.add("progress", progress);
            return o;
        }
        if(object instanceof NumberAdvancementProgress a){
            FileObject o = new FileObject();
            Instant obtained = a.getObtained();
            if(obtained != null) o.add("obtained", registry.serializeToFile(obtained));
            o.addProperty("progress", a.getProgress());
            return o;
        }
        throw new IllegalStateException(object + " is not a supported crux criteria!");
    }

    @Override
    public @Nullable CruxAdvancementProgress deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        throw new UnsupportedOperationException();
    }

    public static @Nullable CruxAdvancementProgress deserialize(@NotNull FileContext<?> ctx, @NotNull FileElement e, @NotNull CruxCriteria criteria){
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        CruxAdvancementProgress progress;
        if(criteria instanceof ListCriteria c){
            ListAdvancementProgress listProgress = new ListAdvancementProgress(c);
            progress = listProgress;

            if(o.get("progress") instanceof FileObject progressMap){
                progressMap.forEach((key, value) ->{
                    SimpleCriterionProgress simple = registry.deserializeFromFile(SimpleCriterionProgress.class, value);
                    if(simple==null) return;
                    listProgress.getProgressMap().put(key, simple);
                });
            }

        }else if(criteria instanceof NumberCriteria c){
            progress = new NumberAdvancementProgress(c);
            Integer prog = o.getObject(Integer.class, "progress");
            if(prog != null) progress.setCriteriaProgress(prog);
        }else throw new IllegalStateException(criteria + " is not a supported crux criteria!");

        Instant obtained = registry.deserializeFromFile(Instant.class, o.get("obtained"));
        if(obtained != null) progress.setObtainedAt(obtained);

        return progress.isEmpty() && !progress.isDone() ? null : progress;
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "crux_advancement_progress";
    }
}
