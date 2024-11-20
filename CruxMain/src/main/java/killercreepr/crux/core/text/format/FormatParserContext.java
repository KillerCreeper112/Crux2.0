package killercreepr.crux.core.text.format;

import killercreepr.crux.core.Crux;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.format.FormatSerializer;
import killercreepr.crux.api.text.provider.StringTagProvider;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.api.text.tags.container.TagContainer;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class FormatParserContext implements TextParserContext {
    @Deprecated(since = "Use TextParserContext", forRemoval = true)
    public static @NotNull Builder builder(){
        return builder(Crux.format());
    }
    @Deprecated(since = "Use TextParserContext", forRemoval = true)
    public static @NotNull Builder builder(@NotNull FormatSerializer format){
        return new Builder(format);
    }
    @Deprecated(since = "Use TextParserContext", forRemoval = true)
    public static @NotNull FormatParserContext empty(@NotNull FormatSerializer format){
        return new FormatParserContext(format, null, null, null);
    }
    @Deprecated(since = "Use TextParserContext", forRemoval = true)
    public static @NotNull FormatParserContext empty(){
        return empty(Crux.format());
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
    public String toString() {
        return "FormatParserContext{format=" + format + ", viewer=" + viewer + ", tagsPrefix=" + tagsPrefix + ", tags=" + tags + "}";
    }

    @Override
    public @NotNull String serialize(@NotNull Component component) {
        return format.serialize(component);
    }

    @Override
    public @NotNull Component deserialize(@NotNull String text) {
        return deserialize(text, tags);
    }

    @Override
    public @NotNull String deserializeString(@NotNull String text) {
        return deserializeString(text, null);
    }

    @Override
    public @NotNull List<Component> deserializeList(@NotNull Collection<String> list) {
        return deserializeList(list, null);
    }

    @Override
    public @NotNull List<String> deserializeStringList(@NotNull Collection<String> list) {
        return deserializeStringList(list, null);
    }

    @Override
    public @NotNull Component deserialize(@NotNull String text, @Nullable StringTagProvider tags) {
        return format.deserialize(text, StringTagProvider.merge(this.tags, tags));
    }

    @Override
    public @NotNull String deserializeString(@NotNull String text, @Nullable StringTagProvider tags) {
        return format.deserializeString(text, StringTagProvider.merge(this.tags, tags));
    }

    @Override
    public @NotNull List<Component> deserializeList(@NotNull Collection<String> list, @Nullable MergedTagContainer tags) {
        return format.deserializeList(list, TagContainer.merged(this.format.tags())
            .addAll(this.tags)
            .addAll(tags));
    }

    @Override
    public @NotNull List<String> deserializeStringList(@NotNull Collection<String> list, @Nullable MergedTagContainer tags) {
        return format.deserializeStringList(list, TagContainer.merged(this.format.tags())
            .addAll(this.tags)
            .addAll(tags));
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
