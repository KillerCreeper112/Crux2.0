package killercreepr.crux.core.text.format;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.parser.ParseException;
import killercreepr.crux.api.registry.Registry;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.format.FormatSerializer;
import killercreepr.crux.api.text.provider.StringTagProvider;
import killercreepr.crux.api.text.resolver.StringListResolver;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.data.util.Pair;
import killercreepr.crux.core.registry.SimpleRegistry;
import killercreepr.crux.core.text.container.SimpleStringTagProvider;
import killercreepr.crux.core.text.container.StringListTagContainer;
import killercreepr.crux.core.text.container.StringTagContainer;
import killercreepr.crux.core.text.tags.standard.NumberFormatResolver;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.crux.core.util.CruxString;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.codehaus.plexus.util.FastMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Format implements FormatSerializer {
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
        return deserialize(text, null);
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
            s = deserializeString(s, tagProvider);
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

    public static List<String> extractAllBracedStrings(String input) {
        List<String> results = new ArrayList<>();
        int braceCount = 0;
        int startIndex = -1;

        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);

            if (currentChar == '{') {
                if (braceCount == 0) {
                    startIndex = i; // Mark the start of the first brace
                }
                braceCount++;
            } else if (currentChar == '}') {
                braceCount--;
                if (braceCount == 0 && startIndex != -1) {
                    results.add(input.substring(startIndex + 1, i)); // Add the content without braces
                    startIndex = -1; // Reset for the next match
                }
            }
        }

        return results; // Return the list of found braced strings
    }

    public @Nullable List<String> matchStringList(@NotNull String text, @NotNull StringListTagContainer container,
                                                  @NotNull TextParserContext context){
        //Matcher matcher = LORE_PATTERN.matcher(text);
        List<String> addon = new ArrayList<>();
        boolean found = false;
        for(String matched : extractAllBracedStrings(text)){
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
        /*while(matcher.find()){
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
        }*/

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
    public static final String placeholderSplit = ":(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
    public @NotNull String processPlaceholders(@NotNull String text, @NotNull TagContainer<StringResolver> tags) {
        return processPlaceholders(text, tags, Map.of(), Set.of());
        /*StringTagContainer resolvers = new StringTagContainer(this.tags);
        resolvers.addAll(STRING_RESOLVERS.values());
        resolvers.addAll(tags);

        if(Crux.debug >= 3){
            Bukkit.broadcastMessage(resolvers.asMap().keySet() + "");
        }

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

        return text;*/
    }

    public @NotNull String processPlaceholders(@NotNull String text, @NotNull TagContainer<StringResolver> tags,
                                               Map<String, String> preUnresolved, Collection<String> preAlready) {
        StringTagContainer resolvers = new StringTagContainer(this.tags);
        resolvers.addAll(STRING_RESOLVERS.values());
        resolvers.addAll(tags);
        StringTagProvider resolveProvider = StringTagProvider.build(resolvers);

        if(Crux.debug >= 3){
            Bukkit.broadcastMessage(resolvers.asMap().keySet() + "");
        }

        //Bukkit.broadcastMessage("before(p): " + text);
        TextParserContext context = new FormatParserContext.Builder(this)
            .build();
        //Bukkit.broadcastMessage("text to process: " + text);
        boolean found;
        Collection<String> already = new HashSet<>(preAlready);
        Map<String, String> unresolved = new FastMap<>(preUnresolved);
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

                /*if(already.contains(placeholder)){
                    continue;
                }*/

                found = !already.contains(placeholder);

                String[] arguments = new String[parts.length - 1];
                System.arraycopy(parts, 1, arguments, 0, parts.length - 1);

                for(int i = 0; i < arguments.length; i++){
                    String arg = arguments[i];
                    if(arg.startsWith("\"") && arg.endsWith("\"")){
                        arg = arg.substring(1, arg.length()-1);
                    }
                    arguments[i] = processPlaceholders(arg, tags, unresolved, already);
                }

                //Bukkit.broadcastMessage("placeholder: " + placeholder + " - " + Arrays.toString(arguments));

                FormatArgs args = new FormatArgs(arguments);
                String replacement = found ? resolvePlaceholder(placeholder, args, context, resolveProvider) : null;
                //Bukkit.broadcastMessage(placeholder + ": " + replacement + " ---   " + Arrays.toString(arguments));
                if(replacement == null){
                    already.add(placeholder);
                    replacement = "<" + placeholder + (args.isEmpty() ? "" : ":" + processPlaceholders(String.join(":", args.getArgs()), resolvers, unresolved, already)) + ">";

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

    private @Nullable String resolvePlaceholder(String placeholderID, FormatArgs args, TextParserContext context, StringTagProvider resolvers) {
        List<String> replacementList = new ArrayList<>();
        StringResolver resolver = resolvers.getStringTags().get(placeholderID);
        if(resolver != null){
            String request = resolver.resolve(args, context);
            if (request != null) {
                // Recursively process nested placeholders
                request = deserializeString(request, resolvers);
                //request = processPlaceholders(request, resolvers);
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
        return processEvalExBool(text, new ArrayDeque<>(), new ArrayDeque<>());
    }

    //todo transfer normal equation {{1+1}} to use this new system as well
    public @NotNull String processEvalExBool(@NotNull String text, @NotNull Deque<Integer> stack, @NotNull Deque<Integer> squareStack) {
        StringBuilder result = new StringBuilder();
        int startingIndex = stack.isEmpty() ? 0 : stack.pop();
        Integer round = null;
        Integer firstSquareIndex = null;
        for (int i = startingIndex; i < text.length(); i++) {
            char c = text.charAt(i);

            if(!squareStack.isEmpty()){
                if(c == ']'){
                    int start = squareStack.pop();
                    firstSquareIndex = start;
                    String fullExpression = text.substring(start+2, i);
                    round = (int) CruxMath.evaluate(processEquations(fullExpression));
                    continue;
                }
                continue;
            }

            if (c == '{') {
                if(i + 2 < text.length() && text.startsWith("e{", i + 1)){
                    stack.push(i);
                }
                continue;
            }

            if(c == '}' && !stack.isEmpty()){
                if(i+1 >= text.length() && round == null) continue;
                char checkChar = round != null ? '}' : text.charAt(i+1);
                if(checkChar == '}'){
                    int start = stack.pop();
                    int end = i;
                    int expressionEnd = firstSquareIndex == null ? end : firstSquareIndex-1;
                    String fullExpression = text.substring(start + 3, expressionEnd); // Extract inside {e{...}}

                    //fullExpression = processEvalExBool(fullExpression);

                    String evaluatedValue;
                    try {
                        evaluatedValue = CruxMath.tryEvaluateEvalEx(fullExpression);
                    } catch (EvaluationException | ParseException e) {
                        if(Crux.debug > 0){
                            Crux.logError("SAD FACE MAN ); " + fullExpression);
                            e.printStackTrace();
                        }
                        continue;
                    }

                    if(round != null){
                        evaluatedValue = NumberFormatResolver.resolve(Double.parseDouble(evaluatedValue), round);
                        squareStack.clear();
                    }

                    result.append(text, 0, start); // Append text before `{e{...}}`
                    result.append(evaluatedValue); // Replace `{e{...}}` with result
                    int endTextIndex;
                    if(firstSquareIndex == null) endTextIndex = end+2;
                    else endTextIndex = end+1;
                    text = text.substring(endTextIndex); // Remove processed `{e{...}}`
                    break;
                }
                if(checkChar == '['){
                    squareStack.push(i);
                    continue;
                }
            }
        }
        result.append(text); // Append remaining text
        text = result.toString();
        return stack.isEmpty() ? text : processEvalExBool(text, stack, squareStack);
    }

    /*public @NotNull String processEvalExBool(@NotNull String text) {
        StringBuilder result = new StringBuilder();
        Stack<Integer> stack = new Stack<>();
        int start = -1;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '{' && i + 2 < text.length() && text.startsWith("e{", i + 1)) {
                stack.push(i);
                if (start == -1) {
                    start = i; // Mark the start of the outermost `{e{`
                }
            } else if (c == '}' && !stack.isEmpty()) {
                int openingIndex = stack.pop();
                if (stack.isEmpty()) {
                    // We've found a full {e{...}} expression
                    int end = i;  // Closing `}` index
                    String fullExpression = text.substring(start + 3, end); // Extract content inside {e{...}}
                    String evaluatedValue;
                    try{
                        evaluatedValue = CruxMath.tryEvaluateEvalEx(fullExpression);
                    }catch (EvaluationException | ParseException e){
                        Crux.logError("SAD FACE MAN );");
                        e.printStackTrace();
                        continue;
                    }

                    result.append(text, 0, start); // Append text before {e{...}}
                    result.append(evaluatedValue); // Replace {e{...}} with evaluated value
                    text = text.substring(end + 2);  // Remove processed `{e{...}}`
                    i = -1; // Restart loop with updated text
                    start = -1;
                }
            }
        }

        result.append(text); // Append remaining text
        return result.toString();

        *//*Matcher matcher = EVAL_EQUATION_PATTERN.matcher(text);
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
                evaluatedValue = CruxMath.tryEvaluateEvalEx(expression) + "";
                if(decimalPlaces != null){
                    try{
                        evaluatedValue = NumberFormatResolver.resolve(Double.parseDouble(evaluatedValue), decimalPlaces);
                    }catch (IllegalArgumentException ignored){}
                }
            } catch (IllegalArgumentException | ParseException | EvaluationException ignored){ continue; }

            matcher.appendReplacement(result, Matcher.quoteReplacement(evaluatedValue));
        }
        matcher.appendTail(result);
        return result.toString();*//*
    }*/

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
