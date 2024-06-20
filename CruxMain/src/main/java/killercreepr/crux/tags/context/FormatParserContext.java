package killercreepr.crux.tags.context;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.crux.tags.format.Format;
import killercreepr.crux.util.CruxItem;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FormatParserContext implements TextParserContext {
    protected final @NotNull Format format;
    protected final @Nullable OfflinePlayer viewer;
    protected final @Nullable FormatPrefix tagsPrefix;
    protected final @Nullable MergedTagContainer tags;

    public FormatParserContext(@NotNull Format format,
                               @Nullable OfflinePlayer viewer,
                               @Nullable FormatPrefix tagsPrefix,
                               @Nullable MergedTagContainer tags) {
        this.format = format;
        this.viewer = viewer;
        this.tagsPrefix = tagsPrefix;
        this.tags = tags;
    }

    @Override
    public @NotNull String parseString(@NotNull String text) {
        return format.deserializeString(text, tags);
        //return format.deserializeString(viewer, tagsPrefix, text, stringTags);
    }

    @Override
    public @NotNull List<String> parseStringLore(@NotNull Collection<String> lore) {
        /*List<String> add = new ArrayList<>();
        for(String s : lore){
            List<String> list = format.deserialize();
            if(list == null){
                list = new ArrayList<>();
                list.add(s);
            }
            for(String input : list){
                add.add(format.deserializeString(viewer, null, input, stringTags));
            }
        }*/
        return format.deserialize(lore, tags);
    }

    @Override
    public @NotNull Component parseComponent(@NotNull String text) {
        return format.deserialize(text, tags);
    }

    @Override
    public @NotNull List<Component> parseComponentLore(@NotNull Collection<String> lore) {
        List<Component> add = new ArrayList<>();
        for(String s : parseStringLore(lore)){
            add.add(CruxItem.NO_ITALICS
                .append(format.deserialize(s, tags)));
            /*List<String> list = format.deserializeLore(viewer, null, s, loreTags);
            if(list == null){
                list = new ArrayList<>();
                list.add(s);
            }
            for(String input : list){
                add.add(CruxItem.NO_ITALICS
                        .append(format.deserialize(viewer, null, input, stringTags)));
            }*/
        }
        return add;
    }

    @Override
    public @Nullable List<String> deserializeLore(@NotNull String input) {
        return null;//todo return format.deserializeLore(viewer, tagsPrefix, input, loreTags);
    }

    @Override
    public @NotNull killercreepr.crux.tags.format.Format getFormat() {
        return null;//todo
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

    public static final class Builder {
        private @NotNull Format format;
        private @Nullable OfflinePlayer viewer;
        private @Nullable FormatPrefix tagsPrefix;
        public Builder(@NotNull Format format) {
            this.format = format;
        }

        public Builder(@NotNull FormatParserContext context){
            viewer(context.getViewer());
            tagsPrefix(context.getTagsPrefix());
        }

        public Builder format(@NotNull Format format) {
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

        public @NotNull FormatParserContext build() {
            return new FormatParserContext(format, viewer, tagsPrefix, null);
        }
    }
}
