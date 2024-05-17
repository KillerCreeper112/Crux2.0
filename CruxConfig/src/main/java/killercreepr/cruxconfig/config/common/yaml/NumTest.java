package killercreepr.cruxconfig.config.common.yaml;

import killercreepr.crux.valueproviders.number.*;
import killercreepr.cruxconfig.config.common.yaml.container.YamlObjectHandler;
import killercreepr.cruxconfig.config.common.yaml.element.YamlArray;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlGeneric;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NumTest implements YamlObjectHandler<NumberProvider> {
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
            if(g.isNumber()) new ConstantNumber(g.getAsNumber());
            return new EquationNumber(g.getAsString());
        }
        if(e instanceof YamlObject map){
            YamlElement value = map.get("value");//todo better implementation
            if(value instanceof YamlGeneric g){
                if(g.isNumber()) new ConstantNumber(value.getAsNumber());
                return new EquationNumber(value.getAsString());
            }
            YamlElement min = map.get("min");
            YamlElement max = map.get("max");
            if(min != null && max != null){
                return new UniformNumber(//todo
                        (NumberProvider) registry.deserialize(NumberProvider.class, min),
                        (NumberProvider) registry.deserialize(NumberProvider.class, max)
                );
            }
        }
        if(e instanceof YamlArray a){
            List<NumberProvider> list = new ArrayList<>();
            for(YamlElement ele : a){
                list.add(
                        (NumberProvider) registry.deserialize(NumberProvider.class, ele)
                );
            }
            return new UniformNumberArray(list.toArray(new NumberProvider[0]));
        }
        return null;
        /*if(e==null) return null;
        YamlRegistry registry = context.getRegistry();
        Map<String, Object> args = e.asMap();
        if(args.size() == 1){
            Object first = args.get(null);
            if(first == null) args.get("value");
            if(first != null){
                if(first instanceof Number n) return new ConstantNumber(n);
                if(first instanceof List<?> d){
                    return new UniformNumberArray(d.toArray(new Number[0]));
                }
            }
        }
        Object min = registry.deserialize(NumberProvider.class, );
        Object max = args.get("max");*/
    }
}
