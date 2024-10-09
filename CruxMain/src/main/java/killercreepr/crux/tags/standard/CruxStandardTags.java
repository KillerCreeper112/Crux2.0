package killercreepr.crux.tags.standard;

import killercreepr.crux.tags.hook.ObjectTag;
import killercreepr.crux.tags.resolver.StringListResolver;
import killercreepr.crux.tags.resolver.StringResolver;
import killercreepr.crux.tags.standard.object.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;

public class CruxStandardTags {
    public static @NotNull Collection<ObjectTag<?>> buildObjectTags(){
        return Set.of(
            new OfflinePlayerTags(),
            new ItemStackTags(),
            new WorldTags(),
            new WorldBorderTags(),
            new BlockTags(),
            new BlockStateTags()
        );
    }

    public static @NotNull Collection<StringResolver> buildGlobalStringTags(){
        return Set.of(
            new LatinFontResolver(),
            new StringFormatResolver(),
            new CruxSpacingResolver(),
            new NumberFormatResolver(),
            new MathsResolver(),
            new DateFormatResolver(),
            new DurationFormatResolver(),
            new CurrentTimeResolver(),
            new ProgressBarResolver()
        );
    }

    public static @NotNull Collection<StringListResolver> buildGlobalStringListTags(){
        return Set.of(
            new DescriptionLoreResolver()
        );
    }

    public static @NotNull Pattern buildStringPattern(){
        return Pattern.compile("<([^<>]+?)>");
    }

    public static @NotNull Pattern buildLorePattern(){
        return Pattern.compile("\\{([^{}]+?)}");
        //return Pattern.compile("\\{([^<>]+?)}");
        //return Pattern.compile("\\{([^{}:/]+(?:/[^{}:/]+)*)?(?::([^{}]+))?}");
         //Pattern.compile("\\{(\\w+)(?::([^{}]+))?}");
    }

    public static @NotNull Pattern buildEquationPattern(){
        //return Pattern.compile("\\{\\{(.+?)}(?:\\[(\\d+)])?(.*?)}");
        return Pattern.compile("\\{\\{(.+?)}(?:\\[(\\d+)])?}");
    }

    public static @NotNull Pattern buildBEquationPattern(){
        //return Pattern.compile("\\{e\\{(.+?)}(?:\\[(\\d+)])?(.*?)}");
        return Pattern.compile("\\{e\\{(.+?)}(?:\\[(\\d+)])?}");
        //return Pattern.compile("\\{e\\{(.+?)\\}\\}");
    }
}
