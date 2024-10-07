package killercreepr.cruxadvancements.config.handler.crazy;

import eu.endercentral.crazy_advancements.advancement.AdvancementVisibility;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileAdvancementVisibility implements FileObjectHandler<AdvancementVisibility> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull AdvancementVisibility object) {
        return new FileObject()
            .addProperty("type", getType(object))
            ;
    }

    @Override
    public @Nullable AdvancementVisibility deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)){
            AdvancementVisibility vis = AdvancementVisibility.parseVisibility(e.getAsString());
            return vis;
        }
        FileRegistry registry = ctx.getRegistry();
        String type = o.getObject(String.class, "type");
        if(type==null) return null;
        switch (type.toLowerCase()){
            case "always" -> {
                return AdvancementVisibility.ALWAYS;
            }
            case "hidden" -> {
                return AdvancementVisibility.HIDDEN;
            }
            case "vanilla" -> {
                return AdvancementVisibility.VANILLA;
            }
            case "parent_granted" -> {
                return AdvancementVisibility.PARENT_GRANTED;
            }
        }
        return null;
    }

    public static @NotNull String getType(@NotNull AdvancementVisibility object){
        if(object.equals(AdvancementVisibility.ALWAYS)) return "always";
        if(object.equals(AdvancementVisibility.HIDDEN)) return "hidden";
        if(object.equals(AdvancementVisibility.VANILLA)) return "vanilla";
        if(object.equals(AdvancementVisibility.PARENT_GRANTED)) return "parent_granted";
        return "custom";
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "crazy_advancement_visibility";
    }
}
