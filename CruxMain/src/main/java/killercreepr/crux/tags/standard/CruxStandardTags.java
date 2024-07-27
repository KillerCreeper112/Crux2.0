package killercreepr.crux.tags.standard;

import killercreepr.crux.tags.hook.ObjectTag;
import killercreepr.crux.tags.resolver.StringListResolver;
import killercreepr.crux.tags.resolver.StringResolver;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Pattern;

public class CruxStandardTags {
    public static @NotNull Collection<ObjectTag<?>> buildObjectTags(){
        return new HashSet<>(){{
            add(new OfflinePlayerTags());
        }};
    }

    public static @NotNull Collection<StringResolver> buildGlobalStringTags(){
        return new HashSet<>(){{
            add(new LatinFontResolver());
            add(new StringFormatResolver());
            add(new CruxSpacingResolver());
            add(new NumberFormatResolver());
        }};
    }

    public static @NotNull Collection<StringListResolver> buildGlobalStringListTags(){
        return new HashSet<>(){{
        }};
    }

    public static @NotNull Pattern buildStringPattern(){
        return Pattern.compile("<([^<>]+?)>");
    }

    public static @NotNull Pattern buildLorePattern(){
        return Pattern.compile("\\{(\\w+)(?::([^{}]+))?}");
    }

    public static @NotNull Pattern buildEquationPattern(){
        return Pattern.compile("\\{\\{(.+?)}(?:\\[(\\d+)])?}");
    }

    public static @NotNull Pattern buildBEquationPattern(){
        return Pattern.compile("\\{e\\{(.+?)}(?:\\[(\\d+)])?}");
        //return Pattern.compile("\\{e\\{(.+?)\\}\\}");
    }
}
