package killercreepr.crux.tags.ab.tags;

import killercreepr.crux.context.FormatParserContext;
import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.tags.FormatArgs;
import killercreepr.crux.tags.ab.StringListTagProvider;
import killercreepr.crux.tags.ab.StringTagProvider;
import killercreepr.crux.tags.ab.container.StringListTagContainer;
import killercreepr.crux.tags.ab.container.StringTagContainer;
import killercreepr.crux.tags.ab.resolver.StringListResolver;
import killercreepr.crux.tags.ab.resolver.StringResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatTest implements FormatSerializer{
    protected final @NotNull MiniMessage miniMessage;
    private final Pattern STRING_PATTERN = Pattern.compile("<([^<>]+?)>");
    private final Pattern LORE_PATTERN = Pattern.compile("\\{(\\w+)(?::([^{}]+))?}");
    public FormatTest(@NotNull MiniMessage miniMessage) {
        this.miniMessage = miniMessage;
    }

    @Override
    public @NotNull String serialize(@NotNull Component component) {
        return miniMessage.serialize(component);
    }

    @Override
    public @NotNull Component deserialize(@NotNull String text) {
        return miniMessage.deserialize(text);
    }

    @Override
    public @NotNull Component deserialize(@NotNull String text, @Nullable StringTagProvider tagProvider) {
        return miniMessage.deserialize(deserializeString(text, tagProvider));
    }

    @Override
    public @NotNull String deserializeString(@NotNull String text, @Nullable StringTagProvider tagProvider) {
        if(tagProvider==null) return text;
        return processPlaceholders(text, tagProvider.getStringTags());
    }

    @Override
    public @NotNull List<String> deserialize(@NotNull Collection<String> list) {
        return new ArrayList<>(list);//todo allow global tags to be registered
    }

    @Override
    public @NotNull List<String> deserialize(@NotNull Collection<String> list, @Nullable StringListTagProvider tagProvider) {
        List<String> formated = new ArrayList<>();
        if(tagProvider==null){
            //todo man
            formated.addAll(list);
            return formated;
        }
        StringListTagContainer container = tagProvider.getStringListTags();
        for(String s : list){
            List<String> found = matchStringList(s, container);
            if(found==null){
                formated.add(s);
                continue;
            }
            formated.addAll(found);
        }
        return formated;
    }

    public @Nullable List<String> matchStringList(@NotNull String text, @NotNull StringListTagContainer container){
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

    private @Nullable List<String> processPlaceholder(@NotNull StringListTagContainer container,
                                                      @NotNull String placeholder,
                                                      @NotNull FormatArgs args) {
        TextParserContext context = new FormatParserContext.Builder(this)
            .loreTags(container)
            .build();
        for(StringListResolver entry : container){
            if(entry.has(placeholder)) return entry.resolve(args, context);
        }
        return null;
    }

    public @NotNull String processPlaceholders(@NotNull String text, @NotNull StringTagContainer resolvers) {
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

    private String resolvePlaceholder(String placeholderID, FormatArgs args, TextParserContext context, StringTagContainer resolvers) {
        List<String> replacementList = new ArrayList<>();
        boolean found = false; // Flag to indicate if any resolution was successful
        for (StringResolver hooked : resolvers) {
            if (!placeholderID.equalsIgnoreCase(hooked.identifier())) continue;
            String request = hooked.resolve(args, context);
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

}
