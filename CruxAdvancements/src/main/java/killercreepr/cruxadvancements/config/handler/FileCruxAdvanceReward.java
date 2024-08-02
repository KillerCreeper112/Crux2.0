package killercreepr.cruxadvancements.config.handler;

import com.google.common.reflect.TypeToken;
import killercreepr.cruxadvancements.advancement.reward.CommandsReward;
import killercreepr.cruxadvancements.advancement.reward.CruxAdvanceReward;
import killercreepr.cruxconfig.config.bukkit.handler.FileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FileCruxAdvanceReward implements FileHandler<CruxAdvanceReward> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull CruxAdvanceReward object) {
        FileObject o = new FileObject();
        FileRegistry registry = ctx.getRegistry();
        if(object instanceof CommandsReward c){
            o.addProperty("type", "console_commands");
            o.add("commands", registry.serializeToFileElement(c.getCommands()));
        }else throw new IllegalArgumentException(object + " is not a supported CruxAdvanceReward!");
        return o;
    }

    @Override
    public @Nullable CruxAdvanceReward deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        String type = o.getObject(String.class, "type");
        if(type==null) return null;
        switch (type.toLowerCase()){
            case "console_commands", "console" ->{
                List<String> commands = registry.deserialize(new TypeToken<List<String>>(){}.getType(), o.get("commands"));
                if(commands == null || commands.isEmpty()) return null;
                return new CommandsReward(commands);
            }
        }
        throw new IllegalStateException(type + " is not a supported crux criteria type!");
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "crux_criteria";
    }
}
