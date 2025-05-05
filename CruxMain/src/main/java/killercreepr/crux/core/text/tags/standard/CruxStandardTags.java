package killercreepr.crux.core.text.tags.standard;

import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringListResolver;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.core.text.tags.standard.object.*;
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
            new BlockStateTags(),
            new BlockDataTags(),
            new CruxPositionTags(),
            new CruxLocationTags(),
            new VectorTags(),
            new LocationTags(),
            new UserTags(),
            new EntityTags(),
            new CommandSenderTags(),
            new ServerTags(),
            new LivingEntityTags(),
            new PotionEffectTags(),
            new ColorTags(),
            new AdvancementTags()
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
            new ProgressBarResolver(),
            new RomanNumeralResolver(),
            new HasOccurredWithinResolver()
        );
    }

    public static @NotNull Collection<StringListResolver> buildGlobalStringListTags(){
        return Set.of(
            new DescriptionLoreResolver(),
            new AddIfLoreResolver(),
            new AddIfElseLoreResolver(),
            new AddIfAllLoreResolver()
        );
    }

    public static @NotNull Pattern buildStringPattern(){
        return Pattern.compile("<((?:[^<>]+|<[^<>]*>)+)>");//Pattern.compile("<([^<>]+?)>");
    }

    public static @NotNull Pattern buildLorePattern(){
        //Pattern.compile("\\{([^{}]+?)}");
        return Pattern.compile("\\{([^{}]+?)}");//Pattern.compile("\\{([^{}]*(?:\\{[^{}]*}[^{}]*)*)}");
        //return Pattern.compile("\\{([^<>]+?)}");
        //return Pattern.compile("\\{([^{}:/]+(?:/[^{}:/]+)*)?(?::([^{}]+))?}");
         //Pattern.compile("\\{(\\w+)(?::([^{}]+))?}");
    }

    public static @NotNull Pattern buildEquationPattern(){
        //return Pattern.compile("\\{\\{(.+?)}(?:\\[(\\d+)])?(.*?)}");
        return Pattern.compile("\\{\\{(.+?)}(?:\\[(\\d+)])?}");
    }

    public static @NotNull Pattern buildBEquationPattern(){
        return Pattern.compile("\\{e\\{([^{}]*+(?:\\{[^{}]*+}[^{}]*+)*?)}(?:\\[(\\d+)])?}");
        //return Pattern.compile("\\{e\\{(.+?)}(?:\\[(\\d+)])?(.*?)}");
        //return Pattern.compile("\\{e\\{(.+?)}(?:\\[(\\d+)])?}");
        //return Pattern.compile("\\{e\\{(.+?)\\}\\}");
    }
}
