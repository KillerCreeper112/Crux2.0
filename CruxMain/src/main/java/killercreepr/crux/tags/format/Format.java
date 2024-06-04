package killercreepr.crux.tags.format;

import killercreepr.crux.context.FormatParserContext;
import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.data.DataExchange;
import killercreepr.crux.data.Holder;
import killercreepr.crux.tags.FormatArgs;
import killercreepr.crux.tags.Tags;
import killercreepr.crux.tags.container.LoreHookContainer;
import killercreepr.crux.tags.container.StringHookContainer;
import killercreepr.crux.tags.hook.string.StringHookedObject;
import killercreepr.crux.tags.tag.LoreResolver;
import killercreepr.crux.util.CruxMath;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Format {
    private final MiniMessage FORMAT;
    private final Tags TAGS;
    private final Collection<RawTextFormat> RAW_TEXT = new HashSet<>();

    private final Pattern STRING_PATTERN = Pattern.compile("<([^<>]+?)>");//Pattern.compile("<(\\w+)(?::([^>]*))?>");//Pattern.compile("<(\\w+)(?::([^>]+))?>");
    private final Pattern LORE_PATTERN = Pattern.compile("\\{(\\w+)(?::([^{}]+))?}");
    private static final Pattern EQUATION_PATTERN = Pattern.compile("\\{\\{(.+?)\\}\\}");
    private static final Pattern B_EQUATION_PATTERN = Pattern.compile("\\{b\\{(.+?)\\}\\}");

    public Format(@NotNull MiniMessage FORMAT, @NotNull Tags TAGS) {
        this.FORMAT = FORMAT;
        this.TAGS = TAGS;
    }

    public @NotNull FormatParserContext simpleContext(){
        return new FormatParserContext.Builder(this).build();
    }

    public @NotNull MiniMessage getFormat() {
        return FORMAT;
    }

    public @NotNull Tags getTags() {
        return TAGS;
    }

    public @NotNull Collection<RawTextFormat> getRawText() {
        return RAW_TEXT;
    }

    public @NotNull Pattern getLorePattern() {
        return LORE_PATTERN;
    }

    public @NotNull RawTextFormat addRawTextFormat(@NotNull RawTextFormat format){
        RAW_TEXT.add(format);
        return format;
    }

    public @NotNull String serialize(@NotNull Component component){
        return FORMAT.serialize(component);
    }

    public @NotNull Component deserialize(@NotNull String text){
        return deserialize(text, null);
    }

    public @NotNull Component deserialize(@NotNull String text, @Nullable StringHookContainer resolvers){
        text = setPlaceholders(text, resolvers);
        /*TextParserContext context = new FormatParserContext.Builder(this)
                .stringTags(resolvers)
                .build();*/
        Collection<TagResolver> tags = new HashSet<>();
        if(resolvers != null) tags.addAll(Arrays.stream(resolvers.buildTagResolvers()).toList());
        return tags.isEmpty() ? FORMAT.deserialize(text) : FORMAT.deserialize(text, tags.toArray(new TagResolver[0]));
    }

    public @NotNull Component deserialize(@Nullable OfflinePlayer viewer, @Nullable FormatPrefix tagsPrefix, @NotNull String text){
        return deserialize(viewer, tagsPrefix, text, null);
    }

    public @NotNull Component deserialize(@Nullable OfflinePlayer viewer, @Nullable FormatPrefix tagsPrefix,
                                          @NotNull String text, @Nullable StringHookContainer resolvers){
        TextParserContext context = new FormatParserContext.Builder(this)
                .viewer(viewer)
                .tagsPrefix(tagsPrefix)
                .stringTags(resolvers)
                .build();
        StringHookContainer container = new StringHookContainer(context, resolvers);
        if(viewer != null) container.putAll(TAGS.hookStringResolvers(context, Holder.directObject(viewer), tagsPrefix));
        return deserialize(formatRawText(viewer, text), container);
    }

    public @NotNull Component deserialize(@NotNull DataExchange info, @Nullable FormatPrefix tagsPrefix, @NotNull String text){
        return deserialize(info, tagsPrefix, text, null);
    }

    public @NotNull Component deserialize(@NotNull DataExchange info, @Nullable FormatPrefix tagsPrefix,
                                          @NotNull String text, @Nullable StringHookContainer resolvers){
        TextParserContext context = new FormatParserContext.Builder(this)
                .tagsPrefix(tagsPrefix)
                .stringTags(resolvers)
                .build();
        StringHookContainer container = new StringHookContainer(context, resolvers);
        container.putAll(TAGS.hookAllTagResolvers(context, info, tagsPrefix));
        OfflinePlayer player = info.getObject(OfflinePlayer.class).orElse(null);
        if(player != null) text = formatRawText(player, text);
        return deserialize(text, container);
    }

    public @NotNull String formatRawText(@Nullable OfflinePlayer player, @NotNull String text){
        for(RawTextFormat f : RAW_TEXT){
            text = f.parse(player, text);
        }
        return text;
    }

    //TEXT
    public @NotNull String deserializeString(@NotNull String text){
        return deserializeString(text, null);
    }

    public @NotNull String deserializeString(@NotNull String text, @Nullable StringHookContainer resolvers){
        return PlainTextComponentSerializer.plainText().serialize(deserialize(text, resolvers));
    }

    public @NotNull String deserializeString(@Nullable OfflinePlayer viewer, @Nullable FormatPrefix tagsPrefix, @NotNull String text){
        return deserializeString(viewer, tagsPrefix, text, null);
    }

    public @NotNull String deserializeString(@Nullable OfflinePlayer viewer,
                                             @Nullable FormatPrefix tagsPrefix,
                                             @NotNull String text, @Nullable StringHookContainer resolvers){
        return PlainTextComponentSerializer.plainText().serialize(deserialize(viewer, tagsPrefix, text, resolvers));
    }

    public @NotNull String deserializeString(@NotNull DataExchange info, @Nullable FormatPrefix tagsPrefix, @NotNull String text){
        return deserializeString(info, tagsPrefix, text, null);
    }

    public @NotNull String deserializeString(@NotNull DataExchange info, @Nullable FormatPrefix tagsPrefix, @NotNull String text, @Nullable StringHookContainer resolvers){
        return PlainTextComponentSerializer.plainText().serialize(deserialize(info, tagsPrefix, text, resolvers));
    }

    public @NotNull String setPlaceholders(@NotNull String text, @Nullable StringHookContainer resolvers){
        if(resolvers == null) return text;
        //Bukkit.broadcastMessage("before: " + text);
        return processEquations(processPlaceholders(text, resolvers));
    }

    public static @NotNull String processEquations(@NotNull String text) {
        Matcher matcher = EQUATION_PATTERN.matcher(text);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            String expression = matcher.group(1);
            String evaluatedValue;
            try{ evaluatedValue = Double.toString(CruxMath.evaluate(expression)); }
            catch (IllegalArgumentException ignored){ continue; }
            matcher.appendReplacement(result, Matcher.quoteReplacement(evaluatedValue));
        }
        matcher.appendTail(result);
        return processEvalExBool(result.toString());
    }

    public static @NotNull String processEvalExBool(@NotNull String text) {
        Matcher matcher = B_EQUATION_PATTERN.matcher(text);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            String expression = matcher.group(1);
            String evaluatedValue;
            try{ evaluatedValue = CruxMath.evaluateEvalEx(expression) + ""; }
            catch (IllegalArgumentException ignored){ continue; }
            matcher.appendReplacement(result, Matcher.quoteReplacement(evaluatedValue));
        }
        matcher.appendTail(result);
        return result.toString();
    }

    public @NotNull String processPlaceholders(@NotNull String text, @NotNull StringHookContainer resolvers) {
        //Bukkit.broadcastMessage("before(p): " + text);
        Matcher matcher = STRING_PATTERN.matcher(text);
        StringBuilder result = new StringBuilder(text.length());
        TextParserContext context = new FormatParserContext.Builder(this)
                .stringTags(resolvers)
                .build();
        while (matcher.find()) {
            String placeholder = matcher.group(1);
            String[] parts = placeholder.split(":");
            placeholder = parts[0];

            String[] arguments = new String[parts.length - 1];
            System.arraycopy(parts, 1, arguments, 0, parts.length - 1);

            //Bukkit.broadcastMessage(placeholder + " placeholder");
            String replacement = resolvePlaceholder(placeholder, new FormatArgs(arguments), context, resolvers);
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);
        //Bukkit.broadcastMessage("after: " + result);
        return result.toString();
    }

    private String resolvePlaceholder(String placeholderID, FormatArgs args, TextParserContext context, StringHookContainer resolvers) {
        List<String> replacementList = new ArrayList<>();
        boolean found = false; // Flag to indicate if any resolution was successful
        for (StringHookedObject<?> hooked : resolvers.get().values()) {
            if (!placeholderID.equalsIgnoreCase(hooked.identifier())) continue;
            String request = hooked.request(args, context);
            if (request != null) {
                found = true; // Resolution was successful
                // Recursively process nested placeholders
                request = processPlaceholders(request, resolvers);
                replacementList.add(request);
            }
        }
        if (!found) {
            // If no resolution was successful, return the original placeholder
            return "<" + placeholderID + (args.isEmpty() ? "" : ":" + processPlaceholders(String.join(":", args.getArgs()), resolvers)) + ">";
        }
        // Return the longest replacement
        return replacementList.stream()
                .max(Comparator.comparingInt(String::length))
                .orElse("<" + placeholderID + (args.isEmpty() ? "" : ":" + processPlaceholders(String.join(":", args.getArgs()), resolvers))+ ">");
    }

    //LORE

    public @Nullable List<String> deserializeLore(@Nullable OfflinePlayer viewer, @Nullable FormatPrefix tagsPrefix, @NotNull String text){
        return deserializeLore(viewer, tagsPrefix, text, null);
    }

    public @Nullable List<String> deserializeLore(@Nullable OfflinePlayer viewer, @Nullable FormatPrefix tagsPrefix, @NotNull String text, @Nullable LoreHookContainer resolvers){
        LoreHookContainer container = new LoreHookContainer(resolvers);
        if(viewer != null) container.putAll(TAGS.hookLoreTags(viewer, tagsPrefix));

        return deserializeLore(formatRawText(viewer, text), container);
    }

    public @Nullable List<String> deserializeLore(@NotNull DataExchange info, @Nullable FormatPrefix tagsPrefix, @NotNull String text){
        return deserializeLore(info, tagsPrefix, text, null);
    }

    public @Nullable List<String> deserializeLore(@NotNull DataExchange info, @Nullable FormatPrefix tagsPrefix, @NotNull String text,
                                                  @Nullable LoreHookContainer resolvers){
        LoreHookContainer container = new LoreHookContainer(resolvers);
        container.putAll(TAGS.hookAllLoreTags(info, tagsPrefix));
        OfflinePlayer player = info.get(OfflinePlayer.class);
        if(player != null) text = formatRawText(player, text);
        return deserializeLore(text, container);
    }

    public @Nullable List<String> deserializeLore(@NotNull String text, @NotNull LoreHookContainer container){
        Matcher matcher = LORE_PATTERN.matcher(text);
        List<String> addon = new ArrayList<>();
        boolean found = false;
        while (matcher.find()) {
            String placeholder = matcher.group(1);
            String optionalParameter = matcher.group(2);

            List<String> addons = processPlaceholder(container,
                    placeholder, new FormatArgs(optionalParameter == null ? new String[0] : optionalParameter.split(":")));
            if(addons != null){
                addon.addAll(addons);
                found = true;
            }
        }
        return found ? addon : null;
    }

    private @Nullable List<String> processPlaceholder(@NotNull LoreHookContainer container,
                                                      @NotNull String placeholder,
                                                      @NotNull FormatArgs args) {
        TextParserContext context = new FormatParserContext.Builder(this)
                .loreTags(container)
                .build();
        for(Map.Entry<String, LoreResolver> entry : container.get().entrySet()){
            if(entry.getKey().equalsIgnoreCase(placeholder)){
                return entry.getValue().resolve(args, context);
            }
        }
        return null;
    }
}
