package killercreepr.cruxattributes.core.text.tags;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxAttributeTags implements ObjectTag<CruxAttribute> {
    @Override
    public @NotNull Class<CruxAttribute> getObjectType() {
        return CruxAttribute.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("crux_attribute_");
    }
    
    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull CruxAttribute object, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("key", (args, ctx) -> object.key().asString()))
            .add(Tag.string("name", (args, ctx) -> object.getName()))
            .add(Tag.string("default_value", (args, ctx) -> object.getDefaultValue() + ""))
            .add(Tag.string("round_multiple", (args, ctx) -> object.getRoundMultiple() + ""))
            ;
    }
}
