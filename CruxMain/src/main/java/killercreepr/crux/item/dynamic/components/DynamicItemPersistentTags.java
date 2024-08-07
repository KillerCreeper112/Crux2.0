package killercreepr.crux.item.dynamic.components;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.item.dynamic.DynamicItemComponent;
import killercreepr.crux.util.CruxItem;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class DynamicItemPersistentTags implements DynamicItemComponent {
    protected final @NotNull Map<Object, DynamicPersistentTag> tags;
    public DynamicItemPersistentTags(@NotNull Map<Object, DynamicPersistentTag> tags) {
        this.tags = tags;
    }

    @Override
    public @NotNull String name() {
        return "persistent_tags";
    }

    public @NotNull Map<Object, DynamicPersistentTag> getTags() {
        return tags;
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull TextParserContext context) {
        tags.forEach((keyObject, tag) -> item.editMeta(meta -> tag.apply(meta, context)));
    }
}
