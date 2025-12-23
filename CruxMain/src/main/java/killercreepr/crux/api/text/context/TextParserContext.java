package killercreepr.crux.api.text.context;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.format.FormatSerializer;
import killercreepr.crux.api.text.provider.StringTagProvider;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.text.format.FormatParserContext;
import killercreepr.crux.core.text.format.WrappedTextParserContext;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public interface TextParserContext extends InputContext {
    static @NotNull Builder builder(){
        return builder(Crux.format());
    }

    static @NotNull Builder builder(@NotNull FormatSerializer format){
        return new FormatParserContext.Builder(format);
    }

    static TextParserContext tags(MergedTagContainer tags){
        return new FormatParserContext(Crux.format(), null, null, tags);
    }

    static @NotNull TextParserContext empty(@NotNull FormatSerializer format){
        if(format.equals(Crux.format())) return EMPTY_CRUX; //optimize
        return new FormatParserContext(format, null, null, null);
    }

    static @NotNull TextParserContext wrapped(@NotNull TextParserContext ctx, @NotNull MergedTagContainer tags){
        return new WrappedTextParserContext(ctx, tags);
    }

    static @NotNull TextParserContext empty(){
        return EMPTY_CRUX;
    }

    TextParserContext EMPTY_CRUX = new FormatParserContext(Crux.format(), null, null, null);

    @Override
    default @NotNull String input(@NotNull String text){
        return deserializeString(text);
    }

    @NotNull String serialize(@NotNull Component component);
    @NotNull Component deserialize(@NotNull String text);

    @NotNull String deserializeString(@NotNull String text);

    @NotNull List<Component> deserializeList(@NotNull Collection<String> list);

    @NotNull List<String> deserializeStringList(@NotNull Collection<String> list);

    @NotNull Component deserialize(@NotNull String text, @Nullable StringTagProvider tags);

    @NotNull String deserializeString(@NotNull String text, @Nullable StringTagProvider tags);

    @NotNull List<Component> deserializeList(@NotNull Collection<String> list, @Nullable MergedTagContainer tags);

    @NotNull List<String> deserializeStringList(@NotNull Collection<String> list, @Nullable MergedTagContainer tags);

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
