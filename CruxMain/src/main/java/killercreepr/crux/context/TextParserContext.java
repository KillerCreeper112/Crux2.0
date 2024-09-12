package killercreepr.crux.context;

import killercreepr.crux.Crux;
import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.crux.tags.context.FormatParserContext;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.format.FormatSerializer;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface TextParserContext {
    static @NotNull Builder builder(){
        return builder(Crux.FORMAT);
    }

    static @NotNull Builder builder(@NotNull FormatSerializer format){
        return new FormatParserContext.Builder(format);
    }
    static @NotNull TextParserContext empty(@NotNull FormatSerializer format){
        return new FormatParserContext(format, null, null, null);
    }

    static @NotNull TextParserContext empty(){
        return empty(Crux.FORMAT);
    }

    @NotNull String serialize(@NotNull Component component);
    @NotNull Component deserialize(@NotNull String text);

    @NotNull String deserializeString(@NotNull String text);

    @NotNull List<Component> deserializeList(@NotNull Collection<String> list);

    @NotNull List<String> deserializeStringList(@NotNull Collection<String> list);

    @NotNull
    FormatSerializer getFormat();
    @Nullable FormatPrefix getPrefix();

    interface Builder {
        Builder format(@NotNull FormatSerializer format);

        Builder viewer(@Nullable OfflinePlayer viewer);

        Builder tagsPrefix(@Nullable FormatPrefix tagsPrefix);

        Builder tags(@Nullable MergedTagContainer tags);

        @NotNull TextParserContext build();
    }
}
