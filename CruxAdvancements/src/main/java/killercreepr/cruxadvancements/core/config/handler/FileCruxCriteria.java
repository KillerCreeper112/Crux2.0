package killercreepr.cruxadvancements.core.config.handler;

import com.google.common.reflect.TypeToken;
import killercreepr.cruxadvancements.api.advancement.criteria.CruxCriteria;
import killercreepr.cruxadvancements.core.advancement.criteria.ListCriteria;
import killercreepr.cruxadvancements.core.advancement.criteria.NumberCriteria;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.element.FilePrimitive;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FileCruxCriteria implements FileObjectHandler<CruxCriteria> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull CruxCriteria object) {
        FileObject o = new FileObject();
        FileRegistry registry = ctx.getRegistry();
        if(object instanceof ListCriteria c){
            o.addProperty("criteria", "list");
            o.add("requirements", registry.serializeToFile(c.getRequirements()));
        }else if(object instanceof NumberCriteria c){
            o.addProperty("criteria", "number");
            o.addProperty("requirements", c.getMaxProgress());
        }else throw new IllegalArgumentException(object + " is not a supported CruxCriteria!");
        return o;
    }

    @Override
    public @Nullable CruxCriteria deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        String type = o.getObject(String.class, "criteria");
        if(type==null) return null;
        switch (type.toLowerCase()){
            case "list" ->{
                if(!(o.get("requirements") instanceof FileArray a)) return null;
                List<String> actionNames = registry.deserializeFromFile(new TypeToken<List<String>>(){}.getType(), o.get("action_names"));
                List<String[]> requirements = new ArrayList<>();
                a.forEach(ele ->{
                    List<String> list = registry.deserializeFromFile(new TypeToken<List<String>>(){}.getType(), ele);
                    if(list==null) return;
                    requirements.add(list.toArray(new String[0]));
                });

                if(actionNames==null){
                    actionNames = new ArrayList<>();
                    for(String[] r : requirements){
                        for(String s : r){
                            if(!actionNames.contains(s)) actionNames.add(s);
                        }
                    }
                }

                return new ListCriteria(actionNames.toArray(new String[0]), requirements.toArray(new String[][]{}));
            }
            case "number" ->{
                if(!(o.get("requirements") instanceof FilePrimitive a)) return null;
                return new NumberCriteria(a.getAsInt());
            }
        }
        throw new IllegalStateException(type + " is not a supported crux criteria type!");
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "crux_criteria";
    }
}
