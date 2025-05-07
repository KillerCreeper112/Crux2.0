package killercreepr.crux.api.valueproviders.number;

import killercreepr.crux.api.data.Holder;
import killercreepr.crux.api.text.context.InputContext;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.crux.core.valueproviders.number.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public interface NumberProvider extends NumberHolder {
    NumberProvider ZERO = constant(0);
    static @NotNull NumberProvider zero(){
        return ZERO;
    }

    static @NotNull NumberProvider holder(@NotNull Holder<Number> holder){
        return new HolderNumber(holder);
    }
    static @NotNull NumberProvider constant(@NotNull Number number){
        return new ConstantNumber(number);
    }
    static @NotNull NumberProvider uniform(@NotNull Number min, @NotNull Number max){
        return new UniformNumber(min, max);
    }
    static @NotNull NumberProvider uniform(@NotNull NumberProvider min, @NotNull NumberProvider max){
        return new UniformNumber(min, max);
    }
    static @NotNull NumberProvider uniformSkewed(@NotNull Number min, @NotNull Number max, @NotNull Number skew){
        return new UniformSkewedNumber(min, max, skew);
    }
    static @NotNull NumberProvider uniformSkewed(@NotNull NumberProvider min, @NotNull NumberProvider max, @NotNull NumberProvider skew){
        return new UniformSkewedNumber(min, max, skew);
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

    static @NotNull String serializeToString(@NotNull NumberProvider provider){
        return switch (provider) {
            case ConstantNumber n ->
                BigDecimal.valueOf(n.getConstant().doubleValue()).stripTrailingZeros().toPlainString();
            case EquationNumber n -> n.getEquation();
            case UniformSkewedNumber n -> serializeToString(n.getMinInclusive()) + "," + serializeToString(n.getMaxInclusive()) + ", " + serializeToString(n.getSkew());
            case UniformNumber n ->
                serializeToString(n.getMinInclusive()) + "," + serializeToString(n.getMaxInclusive());
            case UniformNumberArray n -> "[" + Arrays.stream(n.getNumbers())
                .map(NumberProvider::serializeToString)
                .collect(Collectors.joining(",")) + "]";
            default -> BigDecimal.valueOf(provider.value().doubleValue()).stripTrailingZeros().toPlainString();
        };
    }

    static @NotNull NumberProvider parseFromString(@NotNull String text){
        try{
            return NumberProvider.constant(Double.parseDouble(text));
        }catch (IllegalArgumentException ignored){}
        if(text.startsWith("\"")) text = text.substring(1);
        if(text.endsWith("\"")) text = text.substring(0, text.length()-1);

        if(text.startsWith("[") && text.endsWith("]")){
            List<String> elements = parseElements(text.substring(1, text.length() - 1));
            List<NumberProvider> array = new ArrayList<>();
            for(String s : elements){
                NumberProvider provider = NumberProvider.parseFromString(s);
                array.add(provider);
            }
            return NumberProvider.uniformArray(array.toArray(new NumberProvider[0]));
        }

        String[] split = text.split(",");
        if(split.length > 1){
            if(split.length > 2){
                return NumberProvider.uniformSkewed(
                    NumberProvider.parseFromString(split[0]),
                    NumberProvider.parseFromString(split[1]),
                    NumberProvider.parseFromString(split[2])
                );
            }
            return NumberProvider.uniform(
                NumberProvider.parseFromString(split[0]), NumberProvider.parseFromString(split[1])
            );
        }

        if(text.startsWith("~")){
            int ticks = parseDurationInTicks(text.substring(1));
            return constant(ticks);
        }
        if(text.startsWith("dur=")){
            int ticks = parseDurationInTicks(text.substring(4));
            return constant(ticks);
        }

        return new EquationNumber(text);
    }

    Pattern DURATION_PATTERN = Pattern.compile("(\\d+(\\.\\d+)?)([dhmst]|days?|hours?|minutes?|seconds?|ticks?)");
    private static int parseDurationInTicks(String durationStr) {
        durationStr = durationStr.replaceAll(" ", "");
        Matcher matcher = DURATION_PATTERN.matcher(durationStr);

        double totalTicks = 0;

        // Loop through each match
        while (matcher.find()) {
            double value = (int) Double.parseDouble(matcher.group(1));
            String unit = matcher.group(3);

            // Convert the value to seconds based on the unit
            switch (unit) {
                case "d":
                case "day":
                case "days":
                    totalTicks += value * 1728000D; // 1 day = 1728000 ticks
                    break;
                case "h":
                case "hour":
                case "hours":
                    totalTicks += value * 72000D;  // 1 hour = 72000 ticks
                    break;
                case "m":
                case "min":
                case "minute":
                case "minutes":
                    totalTicks += value * 1200D;     // 1 minute = 1200 ticks
                    break;
                case "s":
                case "sec":
                case "second":
                case "seconds":
                    totalTicks += value * 20D;          // 1 second = 20 ticks
                    break;
                case "t":
                case "tick":
                case "ticks":
                case "tic":
                    totalTicks += value; //ticks
                    break;
                default:
                    throw new IllegalArgumentException("Invalid time unit: " + unit);
            }
        }
        return (int) totalTicks;
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
        if (!currentElement.isEmpty()) {
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

    default @NotNull List<Number> sampleList(@Nullable InputContext ev){
        return sampleList(CruxMath.RANDOM, ev);
    }

    default @NotNull Number sample(@Nullable InputContext ev){
        return sample(CruxMath.RANDOM, ev);
    }
    default @NotNull Number sample(@NotNull Random random){
        return sample(random, null);
    }
    default @NotNull Number sample(){ return sample(CruxMath.RANDOM); }
}
