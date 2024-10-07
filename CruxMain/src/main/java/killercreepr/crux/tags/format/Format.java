package killercreepr.crux.tags.format;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.data.util.Pair;
import killercreepr.crux.registry.Registry;
import killercreepr.crux.registry.SimpleRegistry;
import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.crux.tags.container.StringListTagContainer;
import killercreepr.crux.tags.container.StringTagContainer;
import killercreepr.crux.tags.container.TagContainer;
import killercreepr.crux.tags.context.FormatParserContext;
import killercreepr.crux.tags.provider.StringTagProvider;
import killercreepr.crux.tags.resolver.StringListResolver;
import killercreepr.crux.tags.resolver.StringResolver;
import killercreepr.crux.tags.standard.NumberFormatResolver;
import killercreepr.crux.util.CruxMath;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.codehaus.plexus.util.FastMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Format implements FormatSerializer{
    protected final @NotNull MiniMessage miniMessage;
    protected final @NotNull TagParser tags;
    //todo protected final Registry<RawTextFormat> rawTextFormats = SimpleRegistry.fromSet();
    protected final Pattern STRING_PATTERN;
    protected final Pattern LORE_PATTERN;
    protected final Pattern EQUATION_PATTERN;
    protected final Pattern EVAL_EQUATION_PATTERN;

    protected final Registry<StringResolver> STRING_RESOLVERS = SimpleRegistry.fromSet();
    protected final Registry<StringListResolver> STRING_LIST_RESOLVERS = SimpleRegistry.fromSet();

    public Format(
        @NotNull MiniMessage miniMessage,
        @NotNull TagParser tags,
        @NotNull Pattern stringPattern,
        @NotNull Pattern lorePattern,
        @NotNull Pattern equationPattern,
        @NotNull Pattern evalEquationPattern
    ) {
        this.miniMessage = miniMessage;
        this.tags = tags;
        this.STRING_PATTERN = stringPattern;
        this.LORE_PATTERN = lorePattern;
        this.EQUATION_PATTERN = equationPattern;
        this.EVAL_EQUATION_PATTERN = evalEquationPattern;
    }

    /*public Registry<RawTextFormat> getRawTextFormats() {
        return rawTextFormats;
    }*/

    public @NotNull TagParser tags() {
        return tags;
    }

    @Override
    public @NotNull String serialize(@NotNull Component component) {
        return miniMessage.serialize(component);
    }

    /*@Override
    public @NotNull Component deserialize(@Nullable OfflinePlayer viewer, @NotNull String text){
        return deserialize(viewer, text, null);
    }
    @Override
    public @NotNull Component deserialize(@Nullable OfflinePlayer viewer, @NotNull String text, @Nullable StringTagProvider tagProvider){
        StringTagProvider provider = () -> new StringTagContainer(tags).hook(viewer).addAll(
            tagProvider==null?null:tagProvider.getStringTags()
        );
        return deserialize(text, provider);
    }*/

    @Override
    public @NotNull Component deserialize(@NotNull String text) {
        return miniMessage.deserialize(text);
    }

    @Override
    public @NotNull Component deserialize(@NotNull String text, @Nullable StringTagProvider tagProvider) {
        return miniMessage.deserialize(deserializeString(text, tagProvider));
    }

    @Override
    public @NotNull String deserializeString(@NotNull String text) {
        return deserializeString(text, null);
    }

    @Override
    public @NotNull String deserializeString(@NotNull String text, @Nullable StringTagProvider tagProvider) {
        return processEquations(processPlaceholders(text, tagProvider == null ? new StringTagContainer(tags) : tagProvider.getStringTags()));
    }

    @Override
    public @NotNull List<Component> deserializeList(@NotNull Collection<String> list) {
        return deserializeList(list, null);
    }

    @Override
    public @NotNull List<Component> deserializeList(@NotNull Collection<String> list, @Nullable MergedTagContainer tagProvider) {
        List<Component> formatted = new ArrayList<>();
        for(String s : deserializeStringList(list, tagProvider)){
            formatted.add(deserialize(s, tagProvider));
        }
        return formatted;
    }

    @Override
    public @NotNull List<String> deserializeStringList(@NotNull Collection<String> list) {
        return deserializeStringList(list, null);
    }

    @Override
    public @NotNull List<String> deserializeStringList(@NotNull Collection<String> list, @Nullable MergedTagContainer tagProvider) {
        List<String> formated = new ArrayList<>();
        StringListTagContainer container = new StringListTagContainer(tags);
        container.addAll(STRING_LIST_RESOLVERS.values());
        container.addAll(tagProvider==null?null:tagProvider.getStringListTags());
        /*if(tagProvider==null){
            //add global tags
            formated.addAll(list);
            return formated;
        }*/
        TextParserContext ctx = TextParserContext.builder(this)
            .tags(tagProvider)
            .build();
        for(String s : list){
            List<String> found = matchStringList(s, container, ctx);
            if(found==null){
                formated.add(s);
                continue;
            }
            formated.addAll(found);
        }
        return formated;
    }

    @Override
    public @Nullable List<String> parseStringList(@NotNull String text) {
        return parseStringList(text, null);
    }

    @Override
    public @Nullable List<String> parseStringList(@NotNull String text, @Nullable MergedTagContainer tagProvider) {
        TextParserContext ctx = TextParserContext.builder(this)
            .tags(tagProvider)
            .build();
        return matchStringList(text, new StringListTagContainer(tags).addAll(tagProvider==null?null:tagProvider.getStringListTags()), ctx);
    }

    @Override
    public @NotNull Registry<StringResolver> globalStringResolvers() {
        return STRING_RESOLVERS;
    }

    @Override
    public @NotNull Registry<StringListResolver> globalStringListResolvers() {
        return STRING_LIST_RESOLVERS;
    }

    public @Nullable List<String> matchStringList(@NotNull String text, @NotNull StringListTagContainer container,
                                                  @NotNull TextParserContext context){
        Matcher matcher = LORE_PATTERN.matcher(text);
        List<String> addon = new ArrayList<>();
        boolean found = false;
        while(matcher.find()){
            String matched = matcher.group(1);
            String[] parts = matched.split(placeholderSplit);
            String placeholder = parts[0];

            String[] arguments = new String[parts.length - 1];
            System.arraycopy(parts, 1, arguments, 0, parts.length - 1);

            for(int i = 0; i < arguments.length; i++){
                String arg = arguments[i];
                if(arg.startsWith("\"") && arg.endsWith("\"")){
                    arg = arg.substring(1, arg.length()-1);
                }
                arguments[i] = arg;
            }
            FormatArgs args = new FormatArgs(arguments);
            Pair<List<String>, Boolean> addons = processListPlaceholder(container,
                placeholder, args, context);

            if(addons != null){
                List<String> first = addons.getFirst();
                if(first != null){
                    first.forEach(s ->{
                        addon.add(
                            text.replace("{" + matched + "}", s)
                        );
                    });
                }
                found = true;
            }
        }

        /*while (matcher.find()) {
            String placeholder = matcher.group(1);
            String optionalParameter = matcher.group(2);

            Pair<List<String>, Boolean> addons = processListPlaceholder(container,
                placeholder, new FormatArgs(optionalParameter == null ? new String[0] : optionalParameter.split(":")));
            if(addons != null){
                List<String> first = addons.getFirst();
                //Bukkit.broadcastMessage("first=" + first);
                if(first != null){
                    first.forEach(s ->{
                        //Bukkit.broadcastMessage("replacing=" + "{" + placeholder + (optionalParameter == null ? "" : ":" + optionalParameter) + "}");
                        //Bukkit.broadcastMessage("reaplcing_WITH=" + s);
                        addon.add(
                            text.replace("{" + placeholder + (optionalParameter == null ? "" : ":" + optionalParameter) + "}", s)
                        );
                    });
                    //addon.addAll(first);
                }
                found = true;
            }
        }*/
        return found ? addon : null;
    }

    private @Nullable Pair<List<String>, Boolean> processListPlaceholder(@NotNull StringListTagContainer container,
                                                          @NotNull String placeholder,
                                                          @NotNull FormatArgs args, @NotNull TextParserContext context) {
        StringListResolver resolver = container.get(placeholder);
        //Bukkit.broadcastMessage("resolver=" + resolver + ", from= " + placeholder );
        if(resolver == null) return null;
        return new Pair<>(resolver.resolve(args, context), true);
    }

    //todo possibly:
    //So I fixed nested placeholders like:
    //<claim_setting:<itemholder_setting>>
    //
    //However, if you input a placeholder that is not able to be parsed like this:
    //<claim_setting:<itemholder_setting>:<test>>
    //The entire <claim_setting> won't be parsed.
    //
    //IDK maybe it's fine. We will see!
    //
    //... its not fine );
    //todo FIXED IT :D
    private static final String placeholderSplit = ":(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
    public @NotNull String processPlaceholders(@NotNull String text, @NotNull TagContainer<StringResolver> tags) {
        StringTagContainer resolvers = new StringTagContainer(this.tags);
        resolvers.addAll(STRING_RESOLVERS.values());
        resolvers.addAll(tags);

        //Bukkit.broadcastMessage("before(p): " + text);
        TextParserContext context = new FormatParserContext.Builder(this)
            .build();
        //Bukkit.broadcastMessage("text to process: " + text);
        boolean found;
        Collection<String> already = new HashSet<>();
        Map<String, String> unresolved = new FastMap<>();
        do{
            Matcher matcher = STRING_PATTERN.matcher(text);
            StringBuilder result = new StringBuilder(text.length());
            found = false;

            while (matcher.find()) {
                String placeholder = matcher.group(1);

                for(Map.Entry<String, String> entry : unresolved.entrySet()){
                    placeholder = placeholder.replace(entry.getKey(), entry.getValue());
                }

                String[] parts = placeholder.split(placeholderSplit);
                placeholder = parts[0];

                if(already.contains(placeholder)) continue;

                found = true;

                String[] arguments = new String[parts.length - 1];
                System.arraycopy(parts, 1, arguments, 0, parts.length - 1);

                for(int i = 0; i < arguments.length; i++){
                    String arg = arguments[i];
                    if(arg.startsWith("\"") && arg.endsWith("\"")){
                        arg = arg.substring(1, arg.length()-1);
                    }
                    arguments[i] = processPlaceholders(arg, tags);
                }

                //Bukkit.broadcastMessage("placeholder: " + placeholder + " - " + Arrays.toString(arguments));

                FormatArgs args = new FormatArgs(arguments);
                String replacement = resolvePlaceholder(placeholder, args, context, resolvers);
                //Bukkit.broadcastMessage(placeholder + ": " + replacement + " ---   " + Arrays.toString(arguments));
                if(replacement == null){
                    already.add(placeholder);
                    replacement = "<" + placeholder + (args.isEmpty() ? "" : ":" + processPlaceholders(String.join(":", args.getArgs()), resolvers)) + ">";

                    //If the placeholder cannot be parsed, generate an unresolved placeholder that
                    //does not contain <>. Put that in a map with the replacement so in the end, it
                    //can replace the unresolved placeholders with their normal <> placeholder.
                    //
                    //For example, if I had: <something:<test>> and <test> could not be parsed,
                    //it will basically make the text "<something:|UNRESOLVED-UUID|>".
                    //Then, when it parses <something>, |UNRESOLVED-UUID| will be turned back into <test> for the
                    //placeholder's arguments. And at the very end of the parsing, it will
                    //also replace all the unresolved placeholders back.
                    //
                    //It needs to do this because if it doesn't,
                    //"String placeholder = matcher.group(1);" will get stuck
                    //on the unparsed placeholder.

                    String replace = "|UNRESOLVED-" + UUID.randomUUID() + "|";
                    unresolved.put(replace, replacement);
                    replacement = replace;
                }
                matcher.appendReplacement(result, replacement);
            }
            matcher.appendTail(result);
            text = result.toString();
        }while(found);
        //Bukkit.broadcastMessage("after: " + result);

        for(Map.Entry<String, String> entry : unresolved.entrySet()){
            text = text.replace(entry.getKey(), entry.getValue());
        }

        return text;
    }

    private @Nullable String resolvePlaceholder(String placeholderID, FormatArgs args, TextParserContext context, TagContainer<StringResolver> resolvers) {
        List<String> replacementList = new ArrayList<>();
        StringResolver resolver = resolvers.get(placeholderID);
        if(resolver != null){
            String request = resolver.resolve(args, context);
            if (request != null) {
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
        if (replacementList.isEmpty()) {
            // If no resolution was successful, return the original placeholder
            return null;
            //return "<" + placeholderID + (args.isEmpty() ? "" : ":" + processPlaceholders(String.join(":", args.getArgs()), resolvers)) + ">";
        }
        // Return the longest replacement
        return replacementList.stream()
            .max(Comparator.comparingInt(String::length))
            .orElse(null/*"<" + placeholderID + (args.isEmpty() ? "" : ":" + processPlaceholders(String.join(":", args.getArgs()), resolvers))+ ">"*/);
    }

    public @NotNull String processEquations(@NotNull String text) {
        Matcher matcher = EQUATION_PATTERN.matcher(text);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            String expression = matcher.group(1);

            String evaluatedValue;

            Integer decimalPlaces = null;
            String group2 = matcher.group(2);
            if (group2 != null) {
                try{
                    decimalPlaces = (int) Double.parseDouble(group2);
                }catch (IllegalArgumentException ignored){}
            }

            try{
                double output = CruxMath.evaluate(expression);

                if(decimalPlaces == null) evaluatedValue = Double.toString(output);
                else{
                    try{
                        evaluatedValue = NumberFormatResolver.resolve(output, decimalPlaces);
                    }catch (IllegalArgumentException ignored){
                        evaluatedValue = Double.toString(output);
                    }
                }
            }
            catch (IllegalArgumentException ignored){ continue; }
            matcher.appendReplacement(result, Matcher.quoteReplacement(evaluatedValue));
        }
        matcher.appendTail(result);
        return processEvalExBool(result.toString());
    }

    public @NotNull String processEvalExBool(@NotNull String text) {
        Matcher matcher = EVAL_EQUATION_PATTERN.matcher(text);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            String expression = matcher.group(1);
            String evaluatedValue;

            Integer decimalPlaces = null;
            String group2 = matcher.group(2);
            if (group2 != null) {
                try{
                    decimalPlaces = (int) Double.parseDouble(group2);
                }catch (IllegalArgumentException ignored){}
            }

            try{
                evaluatedValue = CruxMath.evaluateEvalEx(expression) + "";
                if(decimalPlaces != null){
                    try{
                        evaluatedValue = NumberFormatResolver.resolve(Double.parseDouble(evaluatedValue), decimalPlaces);
                    }catch (IllegalArgumentException ignored){}
                }
            }
            catch (IllegalArgumentException ignored){ continue; }

            matcher.appendReplacement(result, Matcher.quoteReplacement(evaluatedValue));
        }
        matcher.appendTail(result);
        return result.toString();
    }

    public static class Builder implements FormatSerializer.Builder{
        protected MiniMessage miniMessage;
        protected TagParser tagParser;
        protected Pattern stringPattern;
        protected Pattern lorePattern;
        protected Pattern equationPattern;
        protected Pattern bEquationPattern;
        protected final Collection<StringResolver> globalStringTags = new HashSet<>();
        protected final Collection<StringListResolver> globalStringListTags = new HashSet<>();

        public Builder addGlobalStringTag(@NotNull StringResolver tag){
            this.globalStringTags.add(tag);
            return this;
        }

        public Builder addGlobalStringTags(@NotNull Collection<StringResolver> tags){
            this.globalStringTags.addAll(tags);
            return this;
        }

        public Builder addGlobalStringListTag(@NotNull StringListResolver tag){
            this.globalStringListTags.add(tag);
            return this;
        }

        public Builder addGlobalStringListTags(@NotNull Collection<StringListResolver> tags){
            this.globalStringListTags.addAll(tags);
            return this;
        }

        public MiniMessage miniMessage() {
            return miniMessage;
        }

        public Builder miniMessage(MiniMessage miniMessage) {
            this.miniMessage = miniMessage;
            return this;
        }

        public TagParser tagParser() {
            return tagParser;
        }

        public Builder tagParser(TagParser tagParser) {
            this.tagParser = tagParser;
            return this;
        }

        public Collection<StringResolver> globalStringTags() {
            return globalStringTags;
        }

        public Collection<StringListResolver> globalStringListTags() {
            return globalStringListTags;
        }

        public Pattern stringPattern() {
            return stringPattern;
        }

        public Builder stringPattern(Pattern stringPattern) {
            this.stringPattern = stringPattern; return this;
        }

        public Pattern lorePattern() {
            return lorePattern;
        }

        public Builder lorePattern(Pattern lorePattern) {
            this.lorePattern = lorePattern; return this;
        }

        public Pattern equationPattern() {
            return equationPattern;
        }

        public Builder equationPattern(Pattern equationPattern) {
            this.equationPattern = equationPattern; return this;
        }

        public Pattern bEquationPattern() {
            return bEquationPattern;
        }

        public Builder bEquationPattern(Pattern bEquationPattern) {
            this.bEquationPattern = bEquationPattern; return this;
        }

        public @NotNull Format build(){
            Objects.requireNonNull(miniMessage);
            Objects.requireNonNull(tagParser);
            Objects.requireNonNull(stringPattern);
            Objects.requireNonNull(lorePattern);
            Objects.requireNonNull(equationPattern);
            Objects.requireNonNull(bEquationPattern);

            Format format = new Format(miniMessage, tagParser, stringPattern, lorePattern, equationPattern, bEquationPattern);
            globalStringTags.forEach(r -> format.globalStringResolvers().register(r));
            globalStringListTags.forEach(r -> format.globalStringListResolvers().register(r));
            return format;
        }
    }
}
