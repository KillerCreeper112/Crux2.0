package killercreepr.cruxattributes.core.text.tags;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.cruxattributes.api.attribute.CruxAttributeModifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class CruxAttributeModifierTags implements ObjectTag<CruxAttributeModifier> {
    @Override
    public @NotNull Class<CruxAttributeModifier> getObjectType() {
        return CruxAttributeModifier.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("crux_attribute_modifier_");
    }
    
    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull CruxAttributeModifier object, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("key", (args, ctx) -> object.key().asString()))
            .add(Tag.string("path", (args, ctx) -> Arrays.toString(object.getPath())))
            .add(Tag.string("slot", (args, ctx) -> object.getSlotGroup().key() + ""))//todo idk something for this
            .add(Tag.string("amount", (args, ctx) -> object.getAmount() + ""))
            .add(Tag.string("operation", (args, ctx) -> object.getOperation().toString().toLowerCase()))
            ;
    }
}
