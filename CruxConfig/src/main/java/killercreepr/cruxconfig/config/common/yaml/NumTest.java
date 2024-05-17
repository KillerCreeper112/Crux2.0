package killercreepr.cruxconfig.config.common.yaml;

import killercreepr.crux.valueproviders.number.ConstantNumber;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.crux.valueproviders.number.UniformNumber;
import killercreepr.crux.valueproviders.number.UniformNumberArray;
import killercreepr.cruxconfig.config.common.yaml.container.YamlObjectHandler;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NumTest implements YamlObjectHandler<NumberProvider> {
    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull NumberProvider object) {
        Map<String, Object> map = new HashMap<>(){{
        }};
        if(object instanceof ConstantNumber d){
            map = new HashMap<>(){{
                put(null, d);
            }};
        }else if(object instanceof UniformNumber d){
            map = new HashMap<>(){{
                put("min", context.getRegistry().serialize(d.getMinInclusive()).map());
                put("max", context.getRegistry().serialize(d.getMaxInclusive()).map());
            }};
        }
        return new YamlElement(map);
    }

    @Override
    public @Nullable NumberProvider deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
        if(e==null) return null;
        YamlRegistry registry = context.getRegistry();
        Map<String, Object> args = e.map();
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
        Object max = args.get("max");
    }
}
