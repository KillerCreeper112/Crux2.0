package killercreepr.cruxadvancements.config.handler;

import killercreepr.cruxadvancements.advancement.objective.AdvancementObjective;
import killercreepr.cruxadvancements.advancement.objective.BreakBlockObjective;
import killercreepr.cruxconfig.config.bukkit.handler.FileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileAdvancementObjective implements FileHandler<AdvancementObjective> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull AdvancementObjective object) {
        throw new RuntimeException("todo");
    }

    @Override
    public @Nullable AdvancementObjective deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        String type = o.getObject(String.class, "type");
        if(type==null) return null;
        String criterion = o.getObject(String.class, "criterion");
        if(criterion==null) return null;
        return deserializeFromFile(context, e, criterion);
    }

    public static @Nullable AdvancementObjective deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e, @NotNull String criterion) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        String type = o.getObject(String.class, "type");
        if(type==null) return null;
        switch (type.toLowerCase()){
            case "break_block" ->{
                Integer maxProgress = o.getObject(Integer.class, "amount");
                if(maxProgress==null) return null;
                Material material = registry.deserialize(Material.class, o.get("block_type"));
                return new BreakBlockObjective(criterion, maxProgress, material);
            }
        }
        throw new IllegalStateException("AdvancementObject type " + type + " does not exist!");
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "advancement_objective";
    }
}
