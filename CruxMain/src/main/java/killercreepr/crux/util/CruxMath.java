package killercreepr.crux.util;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.parser.ParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redempt.crunch.Crunch;
import redempt.crunch.functional.EvaluationEnvironment;

import java.util.concurrent.ThreadLocalRandom;

public class CruxMath {
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
}
