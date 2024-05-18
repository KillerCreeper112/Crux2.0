package killercreepr.cruxconfig.config.bukkit.data;

import killercreepr.crux.valueproviders.number.NumberProvider;
import org.jetbrains.annotations.Nullable;

public class NumberProviderValue extends CommonValue<NumberProvider> {
    public NumberProviderValue(@Nullable NumberProvider defaultValue) {
        super(NumberProvider.class, defaultValue);
    }

    public NumberProviderValue() {
        this(null);
    }

    /*protected @Nullable NumberProvider tryObject(@Nullable Object o){
        if(o instanceof Number constant) return new ConstantNumber(constant);
        if(o instanceof String equation) return new EquationNumber(equation);
        if(isArrayOfType(o, Number.class)) return new UniformNumberArray((Number[]) o);
        if(isArrayOfType(o, String.class)){
            List<NumberProvider> list = new ArrayList<>();
            for(String s : (List<String>) o){
                list.add(new EquationNumber(s));
            }
            return new UniformNumberArray(list.toArray(new NumberProvider[0]));
        }
        return null;
    }

    @Override
    public @Nullable NumberProvider get(@NotNull CruxConfig cfg, @NotNull String path) {
        FileConfiguration c = cfg.config();
        NumberProvider tried = tryObject(c.get(removeDot(path)));
        if(tried != null) return tried;

        tried = tryObject(c.get(addDot(path) + "value"));
        if(tried != null) return tried;

        if(c.get(addDot(path) + "min") instanceof Number min && c.get(addDot(path) + "max") instanceof Number max){
            return new UniformNumber(min, max);
        }
        return null;
    }

    public static <T> boolean isArrayOfType(Object array, Class<T> elementType) {
        return array != null && array.getClass().isArray() && array.getClass().getComponentType() == elementType;
    }

    @Override
    public void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable NumberProvider object) {
        cfg.set(removeDot(path),null);
        if(object==null) return;
        if(object instanceof ConstantNumber c){
            cfg.set(removeDot(path), c.getConstant());
            return;
        }
        if(object instanceof EquationNumber c){
            cfg.set(removeDot(path), c.getEquation());
            return;
        }
        if(object instanceof UniformNumber c){
            new NumberProviderValue().set(cfg, addDot(path) + "min", c.getMinInclusive());
            new NumberProviderValue().set(cfg, addDot(path) + "max", c.getMaxInclusive());
            return;
        }
        if(object instanceof UniformNumberArray c){
            if(c.getNumbers()[0] instanceof ConstantNumber){
                List<Number> list = new ArrayList<>();
                for(NumberProvider p : c.getNumbers()){
                    list.add(p.value());
                }
                cfg.set(removeDot(path), list.toArray(new Number[0]));
                return;
            }
            if(c.getNumbers()[0] instanceof EquationNumber){
                List<String> list = new ArrayList<>();
                for(NumberProvider p : c.getNumbers()){
                    list.add(((EquationNumber) p).getEquation());
                }
                cfg.set(removeDot(path), list.toArray(new String[0]));
                return;
            }
            throw new RuntimeException("Unsupported number provide class cannot be set in a config! (" + object.getClass() + ")");
        }
        throw new RuntimeException("Unsupported number provide class cannot be set in a config! (" + object.getClass() + ")");
    }*/
}
