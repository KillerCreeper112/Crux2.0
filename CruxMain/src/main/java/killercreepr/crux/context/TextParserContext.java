package killercreepr.crux.context;

import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.format.FormatSerializer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface TextParserContext {
    @NotNull String serialize(@NotNull Component component);
    @NotNull Component deserialize(@NotNull String text);

    @NotNull String deserializeString(@NotNull String text);

    @NotNull List<Component> deserializeList(@NotNull Collection<String> list);

    @NotNull List<String> deserializeStringList(@NotNull Collection<String> list);

    @NotNull
    FormatSerializer getFormat();
    @Nullable FormatPrefix getPrefix();
}
