package killercreepr.crux.context;

import killercreepr.crux.tags.format.Format;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface TextParserContext {
    @NotNull String parseString(@NotNull String text);
    @NotNull List<String> parseStringLore(@NotNull List<String> lore);

    @NotNull
    Component parseComponent(@NotNull String text);
    @NotNull List<Component> parseComponentLore(@NotNull List<String> lore);

    @Nullable List<String> deserializeLore(@NotNull String input);
    @NotNull
    Format getFormat();
}
