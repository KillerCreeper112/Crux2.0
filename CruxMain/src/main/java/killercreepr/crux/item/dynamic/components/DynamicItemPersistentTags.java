package killercreepr.crux.item.dynamic.components;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.item.dynamic.DynamicItemComponent;
import killercreepr.crux.item.dynamic.components.persistence.TypedDynamicPersistentTag;
import killercreepr.crux.util.CruxItem;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class DynamicItemPersistentTags implements DynamicItemComponent {
    protected final @NotNull Collection<TypedDynamicPersistentTag> tags;
    public DynamicItemPersistentTags(@NotNull Collection<TypedDynamicPersistentTag> tags) {
        this.tags = tags;
    }

    @Override
    public @NotNull String name() {
        return "persistent_tags";
    }

    public @NotNull Collection<TypedDynamicPersistentTag> getTags() {
        return tags;
    }

    @Override
    public void apply(@NotNull CruxItem item, @NotNull TextParserContext context) {
        tags.forEach(tag -> item.editMeta(meta -> tag.apply(meta, context)));
    }
}
