package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.valueproviders.number.*;
import killercreepr.cruxconfig.config.bukkit.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@JsonSerializer(id = "number_provider")
public class FileNumberProvider extends SimpleFileHandler<NumberProvider> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull NumberProvider object) {
        FileRegistry registry = context.getRegistry();
        if(object instanceof ConstantNumber d){
            return new FileGeneric(d.getConstant());
        }
        if(object instanceof UniformNumber d){
            FileObject map = new FileObject();
            map.add("min", registry.serializeToFileElement(d.getMinInclusive()));
            map.add("max", registry.serializeToFileElement(d.getMaxInclusive()));
            return map;
        }
        if(object instanceof EquationNumber d){
            return new FileGeneric(d.getEquation());
        }
        if(object instanceof UniformNumberArray d){
            FileArray a = new FileArray(d.getNumbers().length);
            for(NumberProvider p : d.getNumbers()){
                a.add(registry.serializeToFileElement(p));
            }
            return a;
        }
        throw new UnsupportedOperationException("Oof");
    }

    @Override
    public @Nullable NumberProvider deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        FileRegistry registry = context.getRegistry();
        if(e instanceof FileGeneric g){
            if(g.isNumber()) return new ConstantNumber(g.getAsNumber());
            return new EquationNumber(g.getAsString());
        }
        if(e instanceof FileObject map){
            FileElement value = map.get("value");
            if(value instanceof FileGeneric g){
                if(g.isNumber()) new ConstantNumber(value.getAsNumber());
                return new EquationNumber(value.getAsString());
            }
            FileElement min = map.get("min");
            FileElement max = map.get("max");
            if(min != null && max != null){
                return new UniformNumber(
                        registry.deserialize(NumberProvider.class, min),
                        registry.deserialize(NumberProvider.class, max)
                );
            }
        }
        if(e instanceof FileArray a){
            List<NumberProvider> list = new ArrayList<>();
            for(FileElement ele : a){
                NumberProvider value = registry.deserialize(NumberProvider.class, ele);
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
