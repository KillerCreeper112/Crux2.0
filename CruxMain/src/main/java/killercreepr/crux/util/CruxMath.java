package killercreepr.crux.util;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.parser.ParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redempt.crunch.Crunch;
import redempt.crunch.functional.EvaluationEnvironment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CruxMath {
    public static final Random RANDOM = new Random();

    /**
     * @param chance The probability from 0-100.
     */
    public static boolean testChance(double chance) {
        return RANDOM.nextDouble() * 100 < chance;
    }

    public static <T> T getRandom(@NotNull List<T> list){
        if(list.isEmpty()) return null;
        return list.get(random(0, list.size()-1));
    }
    public static double round(double value, int place, @NotNull RoundingMode roundingMode){
        if(place < 0) return value;

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(place, roundingMode);
        return bd.doubleValue();
    }

    public static double round(double value, int place){
        return round(value, place, RoundingMode.HALF_UP);
    }

    public static double round(double value){
        return round(value, 1);
    }

    public static int wrap(int value, int min, int max){
        if(value < min) value = max;
        else if (value > max) value = min;
        return value;
    }

    public static float wrap(float value, float min, float max){
        if(value < min) value = max;
        else if (value > max) value = min;
        return value;
    }

    public static double wrap(double value, double min, double max){
        if(value < min) value = max;
        else if (value > max) value = min;
        return value;
    }

    public static long wrap(long value, long min, long max){
        if(value < min) value = max;
        else if (value > max) value = min;
        return value;
    }

    public static float random(float min, float max){
        return ThreadLocalRandom.current().nextInt((int) (min * 10000), (int) (max * 10000) + 1) * .0001f;
    }

    public static float random(double min, double max){
        return ThreadLocalRandom.current().nextInt((int) (min * 10000), (int) (max * 10000) + 1) * .0001f;
    }

    public static int random(int min, int max){ return ThreadLocalRandom.current().nextInt(min, max + 1); }

    public static int clamp(int value, int min, int max){
        return Math.max(min, Math.min(max, value));
    }

    public static float clamp(float value, float min, float max){
        return Math.max(min, Math.min(max, value));
    }

    public static double clamp(double value, double min, double max){
        return Math.max(min, Math.min(max, value));
    }

    public static long clamp(long value, long min, long max){
        return Math.max(min, Math.min(max, value));
    }

    public static boolean testExpression(@Nullable String eq){
        return ((int) evaluate(eq)) == 1;
    }

    public static boolean testExpression(@Nullable String eq, @NotNull EvaluationEnvironment ev){
        return ((int) evaluate(eq, ev)) == 1;
    }

    public static double evaluate(@Nullable String eq){
        if(eq == null || eq.isBlank()) return 0D;
        return Crunch.compileExpression(eq).evaluate();
    }

    public static double evaluate(@Nullable String eq, @Nullable EvaluationEnvironment ev){
        if(eq == null || eq.isBlank()) return 0D;
        if(ev==null) return evaluate(eq);
        return Crunch.compileExpression(eq, ev).evaluate();
    }

    public static boolean evaluateEvalEx(@Nullable String eq){
        if(eq==null || eq.isBlank()) return false;
        try{
            return new Expression(eq).evaluate().getBooleanValue();
        }catch (EvaluationException | ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int unnumeral(@NotNull String number) {
        if (number.startsWith("M")) return 1000 + unnumeral(number.replaceFirst("M", ""));
        if (number.startsWith("CM")) return 900 + unnumeral(number.replaceFirst("CM", ""));
        if (number.startsWith("D")) return 500 + unnumeral(number.replaceFirst("D", ""));
        if (number.startsWith("CD")) return 400 + unnumeral(number.replaceFirst("CD", ""));
        if (number.startsWith("C")) return 100 + unnumeral(number.replaceFirst("C", ""));
        if (number.startsWith("XC")) return 90 + unnumeral(number.replaceFirst("XC", ""));
        if (number.startsWith("L")) return 50 + unnumeral(number.replaceFirst("L", ""));
        if (number.startsWith("XL")) return 40 + unnumeral(number.replaceFirst("XL", ""));
        if (number.startsWith("X")) return 10 + unnumeral(number.replaceFirst("X", ""));
        if (number.startsWith("IX")) return 9 + unnumeral(number.replaceFirst("IX", ""));
        if (number.startsWith("V")) return 5 + unnumeral(number.replaceFirst("V", ""));
        if (number.startsWith("IV")) return 4 + unnumeral(number.replaceFirst("IV", ""));
        if (number.startsWith("I")) return 1 + unnumeral(number.replaceFirst("I", ""));
        return 0;
    }

    private static @NotNull String numeralConvert(int number){
        if(number<=0) return "";
        if(number>=6000) return "";

        if (number-1000>=0) return "M" + numeralConvert(number-1000);
        if (number-900>=0) return "CM" + numeralConvert(number-900);
        if (number-500>=0) return "D" + numeralConvert(number-500);
        if (number-400>=0) return "CD" + numeralConvert(number-400);
        if (number-100>=0) return "C" + numeralConvert(number-100);
        if (number-90>=0) return "XC" + numeralConvert(number-90);
        if (number-50>=0) return "L" + numeralConvert(number-50);
        if (number-40>=0) return "XL" + numeralConvert(number-40);
        if (number-10>=0) return "X" + numeralConvert(number-10);
        if (number-9>=0) return "IX" + numeralConvert(0);
        if (number-5>=0) return "V" + numeralConvert(number-5);
        if (number-4>=0) return "IV" + numeralConvert(0);
        return "I" + numeralConvert(number-1);
    }

    public static @NotNull String numeral(int number) {
        if(number<=0) return String.valueOf(number);
        if(number>=6000) return String.valueOf(number);
        return numeralConvert(number);
    }

    public static @NotNull String padWithZeroIfSingleDigit(int x){ return (x < 10 ? "0" : "") + new DecimalFormat().format(x); }
}
