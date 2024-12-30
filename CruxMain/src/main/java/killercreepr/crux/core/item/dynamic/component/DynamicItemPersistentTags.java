package killercreepr.crux.core.item.dynamic.component;

import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.item.dynamic.DynamicItemComponent;
import killercreepr.crux.api.item.dynamic.component.persistence.TypedDynamicPersistentTag;
import killercreepr.crux.api.text.context.TextParserContext;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class DynamicItemPersistentTags extends SimpleDynamicItemComponent {
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
