package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FilePrimitive;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@JsonSerializer(id = "equipment_slot_group")
public class FileEquipmentSlotGroup implements FileObjectHandler<EquipmentSlotGroup> {
  @Override
  public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NonNull EquipmentSlotGroup object) {
    return new FilePrimitive(object.toString());
  }

  @Override
  public @Nullable EquipmentSlotGroup deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
    var reg = context.getRegistry();
    var key = reg.deserializeFromFile(String.class, e);
    if(key == null) return null;
    return EquipmentSlotGroup.getByName(key);
  }

  @Override
  public @NotNull String jsonSerializerID() {
    return "equipment_slot_group";
  }
}
