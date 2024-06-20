package killercreepr.crux.tags.standard.minimessage;

import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.time.Duration;

public class DurationTag implements TagResolver {
    @Override
    public @Nullable Tag resolve(@NotNull String name, @NotNull ArgumentQueue arguments, @NotNull Context ctx) throws ParsingException {
        if(!has(name)) return null;
        String rawDuration = arguments.pop().value();
        String format = arguments.pop().value();
        Duration duration = Duration.ofMillis(Long.parseLong(rawDuration));
        return Tag.preProcessParsed(format(duration, format));
    }

    public static @NotNull String format(@NotNull Duration duration, @NotNull String format){
        long days = duration.toDays();
        long hours = (duration.toHours() % 24);
        long minutes = (duration.toMinutes() % 60);
        long seconds = (duration.getSeconds() % 60);
        DecimalFormat x = new DecimalFormat("#,###");
        return format.replace("%days%", x.format(days))
                .replace("%hours%", x.format(hours))
                .replace("%minutes%", x.format(minutes))
                .replace("%seconds%", x.format(seconds));
    }

    @Override
    public boolean has(@NotNull String name) {
        return name.equalsIgnoreCase("duration");
    }
}
