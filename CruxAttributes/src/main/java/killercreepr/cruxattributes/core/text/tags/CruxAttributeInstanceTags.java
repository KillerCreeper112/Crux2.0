package killercreepr.cruxattributes.core.text.tags;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.SimpleObjectTag;
import killercreepr.crux.api.text.resolver.StringResolver;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.cruxattributes.api.attribute.CruxAttributeInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CruxAttributeInstanceTags implements SimpleObjectTag<CruxAttributeInstance> {
    @Override
    public @NotNull Class<CruxAttributeInstance> getObjectType() {
        return CruxAttributeInstance.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("crux_attribute_instance_");
    }
    
    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull CruxAttributeInstance object, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("value", (args, ctx) -> object.getValue() + ""))
            .add(Tag.string("base_value", (args, ctx) -> object.getBaseValue() + ""))
            .add(Tag.string("modifiers", (args, ctx) -> object.getModifiers().size() + ""))
            ;
    }

    @Override
    public @Nullable Map<Object, FormatPrefix> hookObjects(CruxAttributeInstance object) {
        return Map.of(object.getAttribute(), FormatPrefix.simple("attribute/"));
    }
}
