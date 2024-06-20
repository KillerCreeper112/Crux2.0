package killercreepr.crux.tags.ab.tags;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.registry.Registry;
import killercreepr.crux.registry.SimpleRegistry;
import killercreepr.crux.tags.FormatArgs;
import killercreepr.crux.tags.ab.StringListTagProvider;
import killercreepr.crux.tags.ab.StringTagProvider;
import killercreepr.crux.tags.ab.container.StringListTagContainer;
import killercreepr.crux.tags.ab.container.StringTagContainer;
import killercreepr.crux.tags.ab.resolver.StringListResolver;
import killercreepr.crux.tags.ab.resolver.StringResolver;
import killercreepr.crux.tags.format.RawTextFormat;
import killercreepr.crux.util.CruxMath;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Format implements FormatSerializer{
    protected final @NotNull MiniMessage miniMessage;
    protected final @NotNull Tags tags;
    protected final Registry<RawTextFormat> rawTextFormats = SimpleRegistry.fromSet();
    protected final Pattern STRING_PATTERN = Pattern.compile("<([^<>]+?)>");
    protected final Pattern LORE_PATTERN = Pattern.compile("\\{(\\w+)(?::([^{}]+))?}");
    protected final Pattern EQUATION_PATTERN = Pattern.compile("\\{\\{(.+?)\\}\\}");
    protected final Pattern B_EQUATION_PATTERN = Pattern.compile("\\{b\\{(.+?)\\}\\}");

    public Format(@NotNull MiniMessage miniMessage, @NotNull Tags tags) {
        this.miniMessage = miniMessage;
        this.tags = tags;
    }

    public @NotNull Tags tags() {
        return tags;
    }

    @Override
    public @NotNull String serialize(@NotNull Component component) {
        return miniMessage.serialize(component);
    }

    @Override
    public @NotNull Component deserialize(@Nullable OfflinePlayer viewer, @NotNull String text){
        return deserialize(viewer, text, null);
    }
    @Override
    public @NotNull Component deserialize(@Nullable OfflinePlayer viewer, @NotNull String text, @Nullable StringTagProvider tagProvider){
        StringTagProvider provider = () -> new StringTagContainer(tags).hook(viewer).addAll(
            tagProvider==null?null:tagProvider.getStringTags()
        );
        return deserialize(text, provider);
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
        return processEquations(processPlaceholders(text, tagProvider.getStringTags()));
    }

    @Override
    public @NotNull List<String> deserialize(@NotNull Collection<String> list) {
        return new ArrayList<>(list);//todo allow global tags to be registered
    }

    @Override
    public @NotNull List<String> deserialize(@NotNull Collection<String> list, @Nullable StringListTagProvider tagProvider) {
        List<String> formated = new ArrayList<>();
        if(tagProvider==null){
            //todo add global tags
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
        TextParserContext context = new killercreepr.crux.tags.ab.context.FormatParserContext.Builder(this)
            .build();
        for(StringListResolver entry : container){//todo
            //todo if(entry.has(placeholder, context.getPrefix())) return entry.resolve(args, context);
        }
        return null;
    }

    public @NotNull String processPlaceholders(@NotNull String text, @NotNull StringTagContainer resolvers) {
        //Bukkit.broadcastMessage("before(p): " + text);
        Matcher matcher = STRING_PATTERN.matcher(text);
        StringBuilder result = new StringBuilder(text.length());
        TextParserContext context = new killercreepr.crux.tags.ab.context.FormatParserContext.Builder(this)
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

        StringResolver hooked = resolvers.get(placeholderID);
        if(hooked != null){
            String request = hooked.resolve(args, context);
            if (request != null) {
                found = true; // Resolution was successful
                // Recursively process nested placeholders
                request = processPlaceholders(request, resolvers);
                replacementList.add(request);
            }
        }
        /*for (StringResolver hooked : resolvers) {
            if (!placeholderID.equalsIgnoreCase(hooked.identifier())) continue;
            String request = hooked.resolve(args, context);
            if (request != null) {
                found = true; // Resolution was successful
                // Recursively process nested placeholders
                request = processPlaceholders(request, resolvers);
                replacementList.add(request);
            }
        }*/
        if (!found) {
            // If no resolution was successful, return the original placeholder
            return "<" + placeholderID + (args.isEmpty() ? "" : ":" + processPlaceholders(String.join(":", args.getArgs()), resolvers)) + ">";
        }
        // Return the longest replacement
        return replacementList.stream()
            .max(Comparator.comparingInt(String::length))
            .orElse("<" + placeholderID + (args.isEmpty() ? "" : ":" + processPlaceholders(String.join(":", args.getArgs()), resolvers))+ ">");
    }

    public @NotNull String processEquations(@NotNull String text) {
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

    public @NotNull String processEvalExBool(@NotNull String text) {
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
}
