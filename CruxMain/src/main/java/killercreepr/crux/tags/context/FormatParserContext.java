package killercreepr.crux.tags.context;

import killercreepr.crux.Crux;
import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.crux.tags.format.FormatSerializer;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class FormatParserContext implements TextParserContext {
    @Deprecated(since = "Use TextParserContext")
    public static @NotNull Builder builder(){
        return builder(Crux.FORMAT);
    }
    @Deprecated(since = "Use TextParserContext")
    public static @NotNull Builder builder(@NotNull FormatSerializer format){
        return new Builder(format);
    }
    @Deprecated(since = "Use TextParserContext")
    public static @NotNull FormatParserContext empty(@NotNull FormatSerializer format){
        return new FormatParserContext(format, null, null, null);
    }
    @Deprecated(since = "Use TextParserContext")
    public static @NotNull FormatParserContext empty(){
        return empty(Crux.FORMAT);
    }

    protected final @NotNull FormatSerializer format;
    protected final @Nullable OfflinePlayer viewer;
    protected final @Nullable FormatPrefix tagsPrefix;
    protected final @Nullable MergedTagContainer tags;

    public FormatParserContext(@NotNull FormatSerializer format,
                               @Nullable OfflinePlayer viewer,
                               @Nullable FormatPrefix tagsPrefix,
                               @Nullable MergedTagContainer tags) {
        this.format = format;
        this.viewer = viewer;
        this.tagsPrefix = tagsPrefix;
        this.tags = tags;
    }


    @Override
    public @NotNull String serialize(@NotNull Component component) {
        return format.serialize(component);
    }

    @Override
    public @NotNull Component deserialize(@NotNull String text) {
        return format.deserialize(text, tags);
    }

    @Override
    public @NotNull String deserializeString(@NotNull String text) {
        return format.deserializeString(text, tags);
    }

    @Override
    public @NotNull List<Component> deserializeList(@NotNull Collection<String> list) {
        return format.deserializeList(list, tags);
    }

    @Override
    public @NotNull List<String> deserializeStringList(@NotNull Collection<String> list) {
        return format.deserializeStringList(list, tags);
    }

    @Override
    public @NotNull FormatSerializer getFormat() {
        return format;
    }

    @Override
    public @Nullable FormatPrefix getPrefix() {
        return tagsPrefix;
    }

    public @Nullable OfflinePlayer getViewer() {
        return viewer;
    }

    public @Nullable FormatPrefix getTagsPrefix() {
        return tagsPrefix;
    }

    public static final class Builder implements TextParserContext.Builder {
        private @NotNull FormatSerializer format;
        private @Nullable OfflinePlayer viewer;
        private @Nullable FormatPrefix tagsPrefix;
        private @Nullable MergedTagContainer tags;
        public Builder(@NotNull FormatSerializer format) {
            this.format = format;
        }

        public Builder(@NotNull FormatParserContext context){
            this.format = context.getFormat();
            viewer(context.getViewer());
            tagsPrefix(context.getTagsPrefix());
        }

        public Builder format(@NotNull FormatSerializer format) {
            this.format = format;
            return this;
        }

        public Builder viewer(@Nullable OfflinePlayer viewer) {
            this.viewer = viewer;
            return this;
        }

        public Builder tagsPrefix(@Nullable FormatPrefix tagsPrefix) {
            this.tagsPrefix = tagsPrefix;
            return this;
        }

        public Builder tags(@Nullable MergedTagContainer tags) {
            this.tags = tags;
            return this;
        }

        public @NotNull FormatParserContext build() {
            return new FormatParserContext(format, viewer, tagsPrefix, tags);
        }
    }
}
