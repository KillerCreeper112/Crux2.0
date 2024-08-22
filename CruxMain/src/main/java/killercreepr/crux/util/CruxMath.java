package killercreepr.crux.util;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.parser.ParseException;
import org.bukkit.util.Vector;
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
    public static final CruxNumberFormat DECIMAL_FORMAT_0 = new CruxNumberFormat(0);
    public static final CruxNumberFormat DECIMAL_FORMAT_1 = new CruxNumberFormat(1);
    public static final CruxNumberFormat DECIMAL_FORMAT_2 = new CruxNumberFormat(2);
    public static final CruxNumberFormat DECIMAL_FORMAT_3 = new CruxNumberFormat(3);
    public static CruxNumberFormat buildOrGetDecimalFormat(int decimalPlaces){
        return switch (decimalPlaces){
            case 0 -> DECIMAL_FORMAT_0;
            case 1 -> DECIMAL_FORMAT_1;
            case 2 -> DECIMAL_FORMAT_2;
            case 3 -> DECIMAL_FORMAT_3;
            default -> new CruxNumberFormat(decimalPlaces);
        };
    }

    public static @NotNull String format(@NotNull Number number){
        return DECIMAL_FORMAT_1.format(number);
    }

    /**
     * @param dir The direction the object was moving in.
     * @param hit Must be a normalized!
     * @return A reflected vector.
     */
    public static @NotNull Vector reflect(@NotNull Vector dir, @NotNull Vector hit){
        double velocityDotProduct = hit.dot(dir);
        return new Vector(dir.getX() - 2 * velocityDotProduct * hit.getX(),
            dir.getY() - 2 * velocityDotProduct * hit.getY(),
            dir.getZ() - 2 * velocityDotProduct * hit.getZ());
    }

    /**
     * Gravity of a potion = .115
     */
    public static @NotNull Vector parabolicMotion(@NotNull Vector from, @NotNull Vector to, int heightGain, double gravity) {
        // Block locations
        int endGain = to.getBlockY() - from.getBlockY();
        double horizDist = Math.sqrt(from.distanceSquared(to));

        // Height gain
        double maxGain = Math.max(heightGain, (endGain + heightGain));

        // Solve quadratic equation for velocity
        double a = -horizDist * horizDist / (4 * maxGain);
        double c = -endGain;

        double slope = -horizDist / (2 * a) - Math.sqrt(horizDist * horizDist - 4 * a * c) / (2 * a);

        // Vertical velocity
        double vy = Math.sqrt(maxGain * gravity);

        // Horizontal velocity
        double vh = vy / slope;

        // Calculate horizontal direction
        int dx = to.getBlockX() - from.getBlockX();
        int dz = to.getBlockZ() - from.getBlockZ();
        double mag = Math.sqrt(dx * dx + dz * dz);
        double dirx = dx / mag;
        double dirz = dz / mag;

        // Horizontal velocity components
        double vx = vh * dirx;
        double vz = vh * dirz;

        return new Vector(vx, vy, vz);
    }


    public static boolean hasOccurredWithin(long value, int ticks){
        return (System.currentTimeMillis() - (50L * ticks)) <= value;
    }

    public static boolean testChance(double chance){
        return testChance(RANDOM, chance);
    }
    /**
     * @param chance The probability from 0-100.
     */
    public static boolean testChance(@NotNull Random random, double chance) {
        return random.nextDouble() * 100 < chance;
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

    public static float random(float min, float max) {
        return RANDOM.nextInt((int) ((max - min) * 10000) + 1) * 0.0001f + min;
    }

    public static float random(double min, double max) {
        return RANDOM.nextInt((int) ((max - min) * 10000) + 1) * 0.0001f + (float) min;
    }

    public static int random(int min, int max) {
        return RANDOM.nextInt((max - min) + 1) + min;
    }

    public static float random(float min, float max, @NotNull Random random) {
        return random.nextInt((int) ((max - min) * 10000) + 1) * 0.0001f + min;
    }

    public static float random(double min, double max, @NotNull Random random) {
        return random.nextInt((int) ((max - min) * 10000) + 1) * 0.0001f + (float) min;
    }

    public static int random(int min, int max, @NotNull Random random) {
        return random.nextInt((max - min) + 1) + min;
    }

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

    public static String evaluateEvalEx(@Nullable String eq){
        if(eq==null || eq.isBlank()) return "false";
        try{
            return new Expression(eq).evaluate().getStringValue();
        }catch (EvaluationException | ParseException e) {
            e.printStackTrace();
            return "false";
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
