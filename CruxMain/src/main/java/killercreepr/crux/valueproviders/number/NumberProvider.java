package killercreepr.crux.valueproviders.number;

import killercreepr.crux.context.InputContext;
import killercreepr.crux.util.CruxMath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public interface NumberProvider extends NumberHolder {
    static @NotNull NumberProvider constant(@NotNull Number number){
        return new ConstantNumber(number);
    }
    static @NotNull NumberProvider uniform(@NotNull Number min, @NotNull Number max){
        return new UniformNumber(min, max);
    }
    static @NotNull NumberProvider uniform(@NotNull NumberProvider min, @NotNull NumberProvider max){
        return new UniformNumber(min, max);
    }
    static @NotNull NumberProvider equation(@NotNull String equation){
        return new EquationNumber(equation);
    }
    static @NotNull UniformNumberArray uniformArray(@NotNull NumberProvider... array){
        return new UniformNumberArray(array);
    }
    static @NotNull UniformNumberArray uniformArray(@NotNull Number... array){
        return new UniformNumberArray(array);
    }

    static @NotNull NumberProvider range(@NotNull Number min, @NotNull Number max){
        return new RangeNumber(min, max);
    }
    static @NotNull NumberProvider range(@NotNull NumberProvider min, @NotNull NumberProvider max){
        return new RangeNumber(min, max);
    }

    static @NotNull NumberProvider parseFromString(@NotNull String text){
        try{
            return NumberProvider.constant(Double.parseDouble(text));
        }catch (IllegalArgumentException ignored){}

        if(text.startsWith("[") && text.endsWith("]")){
            List<String> elements = parseElements(text.substring(1, text.length() - 1));
            List<NumberProvider> array = new ArrayList<>();
            for(String s : elements){
                NumberProvider provider = NumberProvider.parseFromString(s);
                array.add(provider);
            }
            return NumberProvider.uniformArray(array.toArray(new NumberProvider[0]));
        }

        String[] split = text.split(";");
        if(split.length > 1){
            return NumberProvider.uniform(
                NumberProvider.parseFromString(split[0]), NumberProvider.parseFromString(split[1])
            );
        }
        split = text.split(",");
        if(split.length > 1){
            return NumberProvider.range(
                NumberProvider.parseFromString(split[0]), NumberProvider.parseFromString(split[1])
            );
        }

        return new EquationNumber(text);
    }
    private static List<String> parseElements(@NotNull String input) {
        List<String> elements = new ArrayList<>();
        StringBuilder currentElement = new StringBuilder();
        int bracketCount = 0;

        for (char ch : input.toCharArray()) {
            if (ch == '[') {
                bracketCount++;
            } else if (ch == ']') {
                bracketCount--;
            }

            if (ch == ',' && bracketCount == 0) {
                elements.add(currentElement.toString().trim());
                currentElement.setLength(0); // Reset for the next element
            } else {
                currentElement.append(ch);
            }
        }

        // Add the last element if any
        if (currentElement.length() > 0) {
            elements.add(currentElement.toString().trim());
        }

        return elements;
    }

    @NotNull Number getMinValue();
    @NotNull Number getMaxValue();
    @Override
    default @NotNull Number value(){ return sample(); }

    /**
     * @return Used primarily for equation numbers to allow for variables to be replaced with certain numbers.
     */
    @NotNull Number sample(@NotNull Random random, @Nullable InputContext ev);

    default @NotNull List<Number> sampleList(){
        return sampleList(CruxMath.RANDOM);
    }

    default @NotNull List<Number> sampleList(@NotNull Random random){
        return sampleList(random, null);
    }

    default @NotNull List<Number> sampleList(@NotNull Random random, @Nullable InputContext ev){
        return List.of(sample(random, ev));
    }

    default @NotNull Number sample(@Nullable InputContext ev){
        return sample(CruxMath.RANDOM, ev);
    }
    default @NotNull Number sample(@NotNull Random random){
        return sample(random, null);
    }
    default @NotNull Number sample(){ return sample(CruxMath.RANDOM); }
}
