package killercreepr.cruxadvancements.config.handler;

import killercreepr.cruxadvancements.advancement.objective.progress.NumberObjectiveProgress;
import killercreepr.cruxadvancements.advancement.objective.progress.ObjectiveProgress;
import killercreepr.cruxconfig.config.bukkit.handler.FileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileObjectiveProgress implements FileHandler<ObjectiveProgress> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull ObjectiveProgress object) {
        if(object instanceof NumberObjectiveProgress n){
            return new FileObject()
                .addProperty("type", "number")
                .addProperty("progress", n.getProgress())
                ;
        }
        throw new IllegalStateException(object + " is not a supported ObjectiveProgress!");
    }

    @Override
    public @Nullable ObjectiveProgress deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        String type = o.getObject(String.class, "type");
        if(type==null) return null;
        switch (type.toLowerCase()){
            case "number" ->{
                Integer progress = o.getObject(Integer.class, "progress");
                if(progress==null) return null;
                return new NumberObjectiveProgress(progress);
            }
        }
        throw new IllegalStateException(type + " is not a supported ObjectiveProgress!");
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "objective_progress";
    }
}
