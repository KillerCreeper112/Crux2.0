package killercreepr.crux.context;

import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.format.Format;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface TextParserContext {
    @NotNull String parseString(@NotNull String text);
    @NotNull List<String> parseStringLore(@NotNull Collection<String> lore);

    @NotNull
    Component parseComponent(@NotNull String text);
    @NotNull List<Component> parseComponentLore(@NotNull Collection<String> lore);

    @Nullable List<String> deserializeLore(@NotNull String input);
    @NotNull
    Format getFormat();

    @Nullable
    FormatPrefix getPrefix();
}
