package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.cruxconfig.config.bukkit.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import org.bukkit.attribute.AttributeModifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonSerializer(id = "attribute_modifier")
public class FileAttributeModifier extends SimpleFileHandler<AttributeModifier> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull AttributeModifier object) {
        FileRegistry registry = context.getRegistry();
        return new FileObject()
                .addProperty("name", object.getName())
                .addProperty("amount", object.getAmount())
                .add("operation", registry.serializeToFileElement(object.getOperation()))
                .add("slot", registry.serializeToFileElement(object.getSlotGroup()))
                ;
    }

    @Override
    public @Nullable AttributeModifier deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        //todo make proper deserialization
        return null;
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "attribute_modifier";
    }
}
