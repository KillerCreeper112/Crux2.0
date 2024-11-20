package killercreepr.crux.core.text.tags.standard;

import com.google.common.base.Strings;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.core.text.format.FormatArgs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ProgressBarResolver implements StringResolver {
    @Override
    public @NotNull String identifier() {
        return "progress_bar";
    }

    @Override
    public @Nullable String resolve(@NotNull FormatArgs args, @NotNull TextParserContext ctx) {
        //<progress_bar:-:10:<aqua>:<gray>:0.5:1:0>
        //0 - bar char
        //1 - bar amount
        //2 - fill color
        //3 - empty color
        //4 - value
        //5 - max value
        //6 - min value
        int totalBars = args.parseInt(1);
        float current = args.parseFloat(4);
        float max = args.has(5) ? args.parseFloat(5) : 1f;
        float min = args.has(6) ? args.parseFloat(6) : 0f;

        if(current < min) current = min;
        //(value - min) / (max - min)
        return getProgressBar(current-min, max-min, totalBars, args.get(0), args.get(2), args.get(3));
    }

    public static @NotNull String getProgressBar(float current, float max, int totalBars, @NotNull String symbol,
                                                 @NotNull String completedColor, @NotNull String notCompletedColor){
        float percent = current / max;
        int progressBars = (int) (totalBars * percent);
        return completedColor + Strings.repeat(symbol, progressBars) + notCompletedColor + Strings.repeat(symbol, totalBars - progressBars);
    }
}
