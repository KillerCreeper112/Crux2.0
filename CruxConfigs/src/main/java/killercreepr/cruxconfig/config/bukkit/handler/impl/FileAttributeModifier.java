package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.core.util.CruxKey;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import net.kyori.adventure.key.Key;
import org.bukkit.Registry;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
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
                .add("operation", registry.serializeToFile(object.getOperation()))
                .add("slot", registry.serializeToFile(object.getSlotGroup()))
                ;
    }

    @Override
    public @Nullable AttributeModifier deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry r = context.getRegistry();
        String operation = o.get("operation").getAsString();
        AttributeModifier.Operation op;
        try{
            op = AttributeModifier.Operation.valueOf(operation.toUpperCase());
        } catch (IllegalArgumentException ex) {
            op = switch (operation.toLowerCase()){
                case "multiply" -> AttributeModifier.Operation.MULTIPLY_SCALAR_1;
                case "add" -> AttributeModifier.Operation.ADD_NUMBER;
                default -> null;
            };
        }
        if(op == null) op = AttributeModifier.Operation.ADD_NUMBER;
        EquipmentSlotGroup slot;
        if(o.has("slot")) slot = EquipmentSlotGroup.getByName(o.get("slot").getAsString());
        else slot = EquipmentSlotGroup.ANY;
        if(slot == null) slot = EquipmentSlotGroup.ANY;
        return new AttributeModifier(
            CruxKey.key(r.deserializeFromFile(Key.class, o.get("key"))),
            r.deserializeFromFile(Double.class, o.get("amount")),
            op, slot
        );
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "attribute_modifier";
    }
}
