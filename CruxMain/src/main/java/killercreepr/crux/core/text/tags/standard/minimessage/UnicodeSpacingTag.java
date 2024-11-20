package killercreepr.crux.core.text.tags.standard.minimessage;

import killercreepr.crux.core.util.CruxString;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UnicodeSpacingTag implements TagResolver {

    @Override
    public @Nullable Tag resolve(@NotNull String name, @NotNull ArgumentQueue arguments, @NotNull Context ctx) throws ParsingException {
        if(!has(name)) return null;
        return Tag.preProcessParsed(
            CruxString.unicodeSpacing((int) Double.parseDouble(arguments.pop().value()))
        );
    }

    @Override
    public boolean has(@NotNull String name) {
        return name.equalsIgnoreCase("unicode_spacing") || name.equalsIgnoreCase("uspacing");
    }
}
