package killercreepr.crux.tags.minimessage;

import killercreepr.crux.util.CruxString;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringFormatTag implements TagResolver {
    @Override
    public @Nullable Tag resolve(@NotNull String name, @NotNull ArgumentQueue arguments, @NotNull Context ctx) throws ParsingException {
        if(!has(name)) return null;
        String format = arguments.pop().lowerValue();
        String text = arguments.pop().value();
        switch (format){
            case "title" -> text = CruxString.toTitleCase(text);
            case "lower" -> text = text.toLowerCase();
            case "upper" -> text = text.toUpperCase();
        }
        return Tag.preProcessParsed(text);
    }

    @Override
    public boolean has(@NotNull String name) {
        return name.equalsIgnoreCase("stringformat") || name.equalsIgnoreCase("sformat");
    }
}
