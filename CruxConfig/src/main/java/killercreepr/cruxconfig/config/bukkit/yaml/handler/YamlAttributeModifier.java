package killercreepr.cruxconfig.config.bukkit.yaml.handler;

import killercreepr.cruxconfig.config.common.yaml.context.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;
import org.bukkit.attribute.AttributeModifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class YamlAttributeModifier implements YamlObjectHandler<AttributeModifier> {
    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull AttributeModifier object) {
        YamlRegistry registry = context.getRegistry();
        return new YamlObject()
                .addProperty("name", object.getName())
                .addProperty("amount", object.getAmount())
                .add("operation", registry.serializeObject(object.getOperation()))
                .add("slot", registry.serializeObject(object.getSlotGroup()))
                ;
    }

    @Override
    public @Nullable AttributeModifier deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
        if(!(e instanceof YamlObject o)) return null;
        //todo
        return null;
    }
}
