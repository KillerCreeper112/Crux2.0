package killercreepr.crux.config.bukkit.data;

import killercreepr.crux.Crux;
import killercreepr.crux.config.bukkit.CruxConfig;
import killercreepr.crux.config.bukkit.value.ConfigValue;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;

public class AttributeModifierValue extends ConfigValue<AttributeModifier> {
    public AttributeModifierValue(@Nullable AttributeModifier defaultValue) {
        super(AttributeModifier.class, defaultValue);
    }

    public AttributeModifierValue() {
        this(null);
    }

    @Override
    public @Nullable AttributeModifier get(@NotNull CruxConfig crux, @NotNull String path) {
        FileConfiguration cfg = crux.config();
        String name = cfg.getString(path + "name");
        if(name == null){
            Crux.log(Level.WARNING, "Attribute modifier name not set! (" + crux.file().getAbsolutePath() + ")");
            return null;
        }
        AttributeModifier.Operation operation;
        try{ operation = AttributeModifier.Operation.valueOf(cfg.getString(path + "operation", "").toUpperCase()); }
        catch (IllegalArgumentException e){
            Crux.log(Level.WARNING, "Invalid attribute modifier operation! Must be " +
                    Arrays.asList(AttributeModifier.Operation.values()) + " at (" + crux.file().getAbsolutePath() + ")");
            return null;
        }
        String slotName = cfg.getString(path + "slot");
        EquipmentSlot slot = null;
        if(slotName != null){
            try{ slot = EquipmentSlot.valueOf(slotName.toUpperCase()); }
            catch (IllegalArgumentException e){
                Crux.log(Level.WARNING, "Invalid attribute modifier equipment slot! Must be " +
                        Arrays.asList(EquipmentSlot.values()) + " at (" + crux.file().getAbsolutePath() + ")");
                return null;
            }
        }
        return new AttributeModifier(UUID.randomUUID(), name, cfg.getDouble(path + "amount"), operation, slot);
    }

    @Override
    public void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable AttributeModifier modifier) {
        if(modifier == null) cfg.set(removeDot(path), null);
        else{
            cfg.set(path + "name", modifier.getName());
            cfg.set(path + "amount", modifier.getAmount());
            cfg.set(path + "operation", modifier.getOperation().toString());
            cfg.set(path + "slot", modifier.getSlot() == null ? null : modifier.getSlot().toString());
        }
    }
}
