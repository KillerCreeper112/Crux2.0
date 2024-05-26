package killercreepr.cruxconfig.config.bukkit.yaml.handler;

import killercreepr.crux.valueproviders.number.*;
import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlArray;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlGeneric;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class YamlNumberProvider implements YamlObjectHandler<NumberProvider> {
    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull NumberProvider object) {
        YamlRegistry registry = context.getRegistry();
        if(object instanceof ConstantNumber d){
            return new YamlGeneric(d.getConstant());
        }
        if(object instanceof UniformNumber d){
            YamlObject map = new YamlObject();
            map.add("min", registry.serialize(d.getMinInclusive()));
            map.add("max", registry.serialize(d.getMaxInclusive()));
            return map;
        }
        if(object instanceof EquationNumber d){
            return new YamlGeneric(d.getEquation());
        }
        if(object instanceof UniformNumberArray d){
            YamlArray a = new YamlArray(d.getNumbers().length);
            for(NumberProvider p : d.getNumbers()){
                a.add(registry.serialize(p));
            }
            return a;
        }
        throw new UnsupportedOperationException("Oof");
    }

    @Override
    public @Nullable NumberProvider deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
        if(e==null) return null;
        YamlRegistry registry = context.getRegistry();
        if(e instanceof YamlGeneric g){
            if(g.isNumber()) return new ConstantNumber(g.getAsNumber());
            return new EquationNumber(g.getAsString());
        }
        if(e instanceof YamlObject map){
            YamlElement value = map.get("value");
            if(value instanceof YamlGeneric g){
                if(g.isNumber()) new ConstantNumber(value.getAsNumber());
                return new EquationNumber(value.getAsString());
            }
            YamlElement min = map.get("min");
            YamlElement max = map.get("max");
            if(min != null && max != null){
                return new UniformNumber(
                        registry.deserialize(NumberProvider.class, min),
                        registry.deserialize(NumberProvider.class, max)
                );
            }
        }
        if(e instanceof YamlArray a){
            List<NumberProvider> list = new ArrayList<>();
            for(YamlElement ele : a){
                NumberProvider value = registry.deserialize(NumberProvider.class, ele);
                if(value==null) continue;
                list.add(value);
            }
            return new UniformNumberArray(list.toArray(new NumberProvider[0]));
        }
        return null;
    }
}
