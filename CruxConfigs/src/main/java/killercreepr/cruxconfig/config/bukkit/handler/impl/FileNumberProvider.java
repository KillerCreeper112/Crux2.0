package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.valueproviders.number.UniformNumberArray;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@JsonSerializer(id = "number_provider")
public class FileNumberProvider extends SimpleFileHandler<killercreepr.crux.api.valueproviders.number.NumberProvider> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull killercreepr.crux.api.valueproviders.number.NumberProvider object) {
        FileRegistry registry = context.getRegistry();
        if(object instanceof killercreepr.crux.core.valueproviders.number.ConstantNumber d){
            return new FileGeneric(d.getConstant());
        }
        if(object instanceof killercreepr.crux.core.valueproviders.number.UniformNumber d){
            FileObject map = new FileObject();
            map.add("min", registry.serializeToFile(d.getMinInclusive()));
            map.add("max", registry.serializeToFile(d.getMaxInclusive()));
            return map;
        }
        if(object instanceof killercreepr.crux.core.valueproviders.number.EquationNumber d){
            return new FileGeneric(d.getEquation());
        }
        if(object instanceof killercreepr.crux.core.valueproviders.number.UniformNumberArray d){
            FileArray a = new FileArray(d.getNumbers().length);
            for(killercreepr.crux.api.valueproviders.number.NumberProvider p : d.getNumbers()){
                a.add(registry.serializeToFile(p));
            }
            return a;
        }
        throw new UnsupportedOperationException("Oof");
    }

    @Override
    public @Nullable killercreepr.crux.api.valueproviders.number.NumberProvider deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        FileRegistry registry = context.getRegistry();
        if(e instanceof FileGeneric g){
            if(g.isNumber()) return new killercreepr.crux.core.valueproviders.number.ConstantNumber(g.getAsNumber());
            String text = g.getAsString();
            return killercreepr.crux.api.valueproviders.number.NumberProvider.parseFromString(text);
        }
        if(e instanceof FileObject map){
            FileElement value = map.get("value");
            if(value != null){
                return registry.deserializeFromFile(killercreepr.crux.api.valueproviders.number.NumberProvider.class, value);
            }
            FileElement min = map.get("min");
            FileElement max = map.get("max");
            if(min != null && max != null){
                return new killercreepr.crux.core.valueproviders.number.UniformNumber(
                        registry.deserializeFromFile(killercreepr.crux.api.valueproviders.number.NumberProvider.class, min),
                        registry.deserializeFromFile(killercreepr.crux.api.valueproviders.number.NumberProvider.class, max)
                );
            }
        }
        if(e instanceof FileArray a){
            List<killercreepr.crux.api.valueproviders.number.NumberProvider> list = new ArrayList<>();
            for(FileElement ele : a){
                killercreepr.crux.api.valueproviders.number.NumberProvider value = registry.deserializeFromFile(killercreepr.crux.api.valueproviders.number.NumberProvider.class, ele);
                if(value==null) continue;
                list.add(value);
            }
            return new UniformNumberArray(list.toArray(new NumberProvider[0]));
        }
        return null;
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "number_provider";
    }
}
