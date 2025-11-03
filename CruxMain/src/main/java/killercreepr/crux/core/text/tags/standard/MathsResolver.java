package killercreepr.crux.core.text.tags.standard;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.core.text.format.FormatArgs;
import killercreepr.crux.core.util.CruxMath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MathsResolver implements StringResolver {
    @Override
    public @NotNull String identifier() {
        return "math";
    }

    @Override
    public @Nullable String resolve(@NotNull FormatArgs args, @NotNull TextParserContext context) {
        String function = args.get(0);
        try{
            return resolve(function, args, context);
        }catch (Exception ignored){}
        return null;
    }

    public static @NotNull String resolve(@NotNull String function, @NotNull FormatArgs args, @NotNull TextParserContext ctx){
        switch (function.toLowerCase()){
            case "progress" ->{
                double value = CruxMath.evaluate(ctx.deserializeString(args.get(1)));
                double min = args.has(3) ? CruxMath.evaluate(ctx.deserializeString(args.get(2))) : 0D;
                double max = CruxMath.evaluate(ctx.deserializeString(args.get(
                    args.has(3) ? 3 : 2
                )));

                // Normalize progress between 0 and 1
                double progress = (value - min) / (max - min);
                return CruxMath.clamp(progress, 0D, 1D) + "";
            }
            case "less" ->{
                double first = CruxMath.evaluate(ctx.deserializeString(args.get(1)));
                double second = CruxMath.evaluate(ctx.deserializeString(args.get(2)));
                if(first < second) return ctx.deserializeString(args.get(3));
                return args.has(4) ? ctx.deserializeString(args.get(4)) : "";
            }
            case "rand", "random" ->{
                if(args.has(2)){
                    return CruxMath.random(
                        CruxMath.evaluate(ctx.deserializeString(args.get(1))),
                        CruxMath.evaluate(ctx.deserializeString(args.get(2)))
                    ) + "";
                }
                return CruxMath.random(
                    0D,
                    CruxMath.evaluate(ctx.deserializeString(args.get(1)))
                ) + "";
            }
            case "randsigned", "randomsigned", "random_signed" ->{
                if(args.has(2)){
                    return CruxMath.randomSigned(
                        CruxMath.evaluate(ctx.deserializeString(args.get(1))),
                        CruxMath.evaluate(ctx.deserializeString(args.get(2)))
                    ) + "";
                }
                return CruxMath.randomSigned(
                    0D,
                    CruxMath.evaluate(ctx.deserializeString(args.get(1)))
                ) + "";
            }
            case "randskewed", "randomskewed", "random_skewed" ->{
                if(args.has(3)){
                    return CruxMath.randomSkewed(
                        CruxMath.evaluate(ctx.deserializeString(args.get(1))),
                        CruxMath.evaluate(ctx.deserializeString(args.get(2))),
                        CruxMath.evaluate(ctx.deserializeString(args.get(3)))
                    ) + "";
                }
                if(args.has(2)){
                    return CruxMath.randomSkewed(
                        CruxMath.evaluate(ctx.deserializeString(args.get(1))),
                        CruxMath.evaluate(ctx.deserializeString(args.get(2))),
                        1D
                    ) + "";
                }
                return CruxMath.randomSkewed(
                    0D,
                    CruxMath.evaluate(ctx.deserializeString(args.get(1))), 1D
                ) + "";
            }
            case "randskewedtoward", "randomskewedtoward", "random_skewed_toward" ->{
                return CruxMath.randomSkewedToward(
                    CruxMath.evaluate(ctx.deserializeString(args.get(1))),
                    CruxMath.evaluate(ctx.deserializeString(args.get(2))),
                    CruxMath.evaluate(ctx.deserializeString(args.get(3))),
                    CruxMath.evaluate(ctx.deserializeString(args.get(4)))
                ) + "";
            }
            case "clamp" ->{
                double input = CruxMath.evaluate(ctx.deserializeString(args.get(1)));
                double min = CruxMath.evaluate(ctx.deserializeString(args.get(2)));
                if(args.has(3)){
                    double max = CruxMath.evaluate(ctx.deserializeString(args.get(3)));
                    return CruxMath.clamp(input, min, max) + "";
                }
                return Math.min(min, input) + "";
            }
            case "min" ->{
                double one = CruxMath.evaluate(ctx.deserializeString(args.get(1)));
                double two = CruxMath.evaluate(ctx.deserializeString(args.get(2)));
                return Math.min(one, two) + "";
            }
            case "max" ->{
                double one = CruxMath.evaluate(ctx.deserializeString(args.get(1)));
                double two = CruxMath.evaluate(ctx.deserializeString(args.get(2)));
                return Math.max(one, two) + "";
            }
            case "wrap" ->{
                double value = CruxMath.evaluate(ctx.deserializeString(args.get(1)));
                double min = CruxMath.evaluate(ctx.deserializeString(args.get(2)));
                double max = CruxMath.evaluate(ctx.deserializeString(args.get(3)));
                return CruxMath.wrap(value, min, max) + "";
            }
            case "round" ->{
                double value = CruxMath.evaluate(ctx.deserializeString(args.get(1)));
                int place = args.has(2) ? (int) CruxMath.evaluate(ctx.deserializeString(args.get(2))) : 1;
                return CruxMath.round(value, place) + "";
            }
            case "square" ->{
                double value = CruxMath.evaluate(ctx.deserializeString(args.get(1)));
                return CruxMath.square(value) + "";
            }
            case "sqrt" ->{
                double value = CruxMath.evaluate(ctx.deserializeString(args.get(1)));
                return Math.sqrt(value) + "";
            }
            case "pow" ->{
                double value = CruxMath.evaluate(ctx.deserializeString(args.get(1)));
                double b = CruxMath.evaluate(ctx.deserializeString(args.get(2)));
                return Math.pow(value, b) + "";
            }
            case "floor" ->{
                double value = CruxMath.evaluate(ctx.deserializeString(args.get(1)));
                return Math.floor(value) + "";
            }
            case "ceil" ->{
                double value = CruxMath.evaluate(ctx.deserializeString(args.get(1)));
                return Math.ceil(value) + "";
            }
            case "abs", "absolute" ->{
                double value = CruxMath.evaluate(ctx.deserializeString(args.get(1)));
                return Math.abs(value) + "";
            }
            case "pi" ->{
                return Math.PI + "";
            }
            case "exponent" ->{
                double value = CruxMath.evaluate(ctx.deserializeString(args.get(1)));
                return Math.getExponent(value) + "";
            }
        }
        return "0";
    }
}
