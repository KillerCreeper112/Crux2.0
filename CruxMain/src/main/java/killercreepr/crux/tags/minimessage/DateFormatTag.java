package killercreepr.crux.tags.minimessage;

import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatTag implements TagResolver {
    @Override
    public @Nullable Tag resolve(@NotNull String name, @NotNull ArgumentQueue arguments, @NotNull Context ctx) throws ParsingException {
        if(!has(name)) return null;
        long value = Long.parseLong(arguments.pop().value());
        String format = arguments.pop().value();
        return Tag.preProcessParsed(format(new Date(value), format));
    }

    public static @NotNull String format(@NotNull Date date, @NotNull String format){
        return new SimpleDateFormat(format).format(date);
    }

    @Override
    public boolean has(@NotNull String name) {
        return name.equalsIgnoreCase("date");
    }
}
