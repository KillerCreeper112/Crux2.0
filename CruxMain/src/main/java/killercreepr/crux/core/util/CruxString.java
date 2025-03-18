package killercreepr.crux.core.util;

import com.google.common.collect.ImmutableMap;
import killercreepr.crux.core.registries.CruxRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class CruxString {
    public static boolean parseBoolean(@NotNull String s){
        for(Map.Entry<String, Boolean> entry : CruxRegistries.BOOLEAN_MAPPED.entrySet()){
            if(s.equalsIgnoreCase(entry.getKey())) return entry.getValue();
        }
        return Boolean.parseBoolean(s);
    }

    public static String toTitleCase(@Nullable String string) {
        if(string == null || string.equals("")) return string;
        return Stream.of(string.replaceAll("_", " ").split(" ")).
                map(w -> w.toUpperCase().charAt(0)+ w.toLowerCase().substring(1)).
                reduce((s, s2) -> s + " " + s2).orElse("");
    }

    public static int parseInt(@Nullable String string, int defaultValue){
        if(string == null) return defaultValue;
        try{ return Integer.parseInt(string); } catch (IllegalArgumentException ignored){ return defaultValue; }
    }

    public static @NotNull String join(@NotNull String[] args){
        return join(args, 0);
    }

    public static @NotNull String join(@NotNull String[] args, int start){
        return join(args, start, args.length);
    }

    public static @NotNull String join(@NotNull String[] args, int start, int end){
        return join(args, start, end, " ");
    }

    public static @NotNull String join(@NotNull String[] args, int start, int end, @NotNull String spacing){
        StringBuilder builder = new StringBuilder();
        for(int i = start; i < end; i++){
            String s = args[i];
            builder.append(s);
            if(i+1<end) builder.append(spacing);
        }
        return builder.toString();
    }

    public static int DEFAULT_DESCRIPTION_MAX_LENGTH = 30;
    public static @NotNull List<String> buildDescription(@NotNull String text){
        return buildDescription(text, DEFAULT_DESCRIPTION_MAX_LENGTH);
    }

    public static @NotNull List<String> buildDescription(@NotNull String text, int maxLength){
        List<String> list = new ArrayList<>();
        buildDescription(text, list::add, maxLength);
        return list;
    }

    public static @NotNull List<String> buildDescription(@NotNull String text, int maxLength, @Nullable Function<String, String> function){
        List<String> list = new ArrayList<>();
        buildDescription(text, s ->{
            if(function != null) s = function.apply(s);
            list.add(s);
        }, maxLength);
        return list;
    }

    public static void buildDescription(@NotNull String text, @NotNull Consumer<String> lineConsumer, int maxLength){
        if(text.isEmpty()) return;

        String[] split = text.split(" ");

        StringBuilder current = new StringBuilder();
        for (String word : split) {
            if (current.length() + word.length() + 1 <= maxLength) {
                current.append(word).append(' ');
            } else {
                if (!current.isEmpty()) {
                    String line = current.substring(0, current.length() - 1);
                    lineConsumer.accept(line);
                }
                current = new StringBuilder(word).append(' ');
            }
        }
        if (!current.isEmpty()) {
            String s = current.substring(0, current.length() - 1);
            lineConsumer.accept(s);
        }
    }

    public static @NotNull String[] quoteSplit(@NotNull String input, @NotNull String delimiter) {
        List<String> result = new ArrayList<>();

        // Escape the delimiter for regex if it's a special character
        String regexDelimiter = Pattern.quote(delimiter);
        Pattern pattern = Pattern.compile(
            "[^\\s" + regexDelimiter + "\"]+" +
                "|\"(.*?)\"(?=\\s|$)" +
                "|'(.*?)'(?=\\s|$)"
        );

        //Pattern pattern = Pattern.compile("[^\\s" + regexDelimiter + "\"]+|\"([^\"]*)\"|'([^']*)'");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                // Double-quoted string
                result.add(matcher.group(1));
            } else if (matcher.group(2) != null) {
                // Single-quoted string
                result.add(matcher.group(2));
            } else {
                // Unquoted string
                result.add(matcher.group());
            }
        }

        return result.toArray(new String[0]);
    }

    public static @NotNull String unicodeSpacingFont(int number){
        return "<font:\"crux:spacing\">" + unicodeSpacing(number) + "</font>";
    }

    public static @NotNull String unicodeSpacing(int number){
        if(number == 0) return "";
        return unicodeSpacingConvert(Math.abs(number), number < 0);
    }

    public static @NotNull String unicodeSpacingValue(int specificValue, boolean isNegative){
        return switch (specificValue){
            case 2 -> isNegative ? "\uF802" : "\uF822";
            case 3 -> isNegative ? "\uF803" : "\uF823";
            case 4 -> isNegative ? "\uF804" : "\uF824";
            case 5 -> isNegative ? "\uF805" : "\uF825";
            case 6 -> isNegative ? "\uF806" : "\uF826";
            case 7 -> isNegative ? "\uF807" : "\uF827";
            case 8 -> isNegative ? "\uF808" : "\uF828";
            case 16 -> isNegative ? "\uF809" : "\uF829";
            case 32 -> isNegative ? "\uF80A" : "\uF82A";
            case 64 -> isNegative ? "\uF80B" : "\uF82B";
            case 128 -> isNegative ? "\uF80C" : "\uF82C";
            case 512 -> isNegative ? "\uF80D" : "\uF82D";
            case 1024 -> isNegative ? "\uF80E" : "\uF82E";
            default -> isNegative ? "\uF801" : "\uF821"; //1
        };
    }

    public static @NotNull String unicodeSpacingConvert(int number, boolean isNegative){
        if(number<=0) return "";
        if (number-1024>=0) return unicodeSpacingValue(1024, isNegative) + unicodeSpacingConvert(number-1024,isNegative);
        if (number-512>=0) return unicodeSpacingValue(512, isNegative) + unicodeSpacingConvert(number-512,isNegative);
        if (number-128>=0) return unicodeSpacingValue(128, isNegative) + unicodeSpacingConvert(number-128,isNegative);
        if (number-64>=0) return unicodeSpacingValue(64, isNegative) + unicodeSpacingConvert(number-64,isNegative);
        if (number-32>=0) return unicodeSpacingValue(32, isNegative) + unicodeSpacingConvert(number-32,isNegative);
        if (number-16>=0) return unicodeSpacingValue(16, isNegative) + unicodeSpacingConvert(number-16,isNegative);
        if (number-8>=0) return unicodeSpacingValue(8, isNegative) + unicodeSpacingConvert(number-8,isNegative);
        if (number-7>=0) return unicodeSpacingValue(7, isNegative);
        if (number-6>=0) return unicodeSpacingValue(6, isNegative);
        if (number-5>=0) return unicodeSpacingValue(5, isNegative);
        if (number-4>=0) return unicodeSpacingValue(4, isNegative);
        if (number-3>=0) return unicodeSpacingValue(3, isNegative);
        if (number-2>=0) return unicodeSpacingValue(2, isNegative);
        return unicodeSpacingValue(1, isNegative);
    }

    public final static Map<Character, Character> LATIN_FONT = ImmutableMap.<Character, Character>builder()
        .put('a', 'ᴀ')
        .put('b', 'ʙ')
        .put('c', 'ᴄ')
        .put('d', 'ᴅ')
        .put('e', 'ᴇ')
        .put('f', 'ꜰ')
        .put('g', 'ɢ')
        .put('h', 'ʜ')
        .put('i', 'ɪ')
        .put('j', 'ᴊ')
        .put('k', 'ᴋ')
        .put('l', 'ʟ')
        .put('m', 'ᴍ')
        .put('n', 'ɴ')
        .put('o', 'ᴏ')
        .put('p', 'ᴘ')
        .put('q', 'ꞯ')
        .put('r', 'ʀ')
        .put('s', 'ꜱ')
        .put('t', 'ᴛ')
        .put('u', 'ᴜ')
        .put('v', 'ᴠ')
        .put('w', 'ᴡ')
        .put('y', 'ʏ')
        .put('z', 'ᴢ')
        .build();

    public static char latinFont(char c){
        return LATIN_FONT.getOrDefault(Character.toLowerCase(c), c);
    }

    public static @NotNull String latinFont(@NotNull String s){
        char[] array = s.toCharArray();
        int index = -1;
        for(char c : array){
            index++;
            Character l = LATIN_FONT.get(Character.toLowerCase(c));
            if(l != null) array[index] = l;
        }
        return String.valueOf(array);
    }
}
