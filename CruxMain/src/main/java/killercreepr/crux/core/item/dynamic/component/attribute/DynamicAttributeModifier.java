package killercreepr.crux.core.item.dynamic.component.attribute;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.util.CruxMath;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DynamicAttributeModifier {
    protected final @Nullable Object key;
    protected final @NotNull NumberProvider amount;
    protected final @Nullable Object operation;
    protected final @Nullable Object equipmentSlotGroup;

    public DynamicAttributeModifier(@Nullable Object key, @NotNull NumberProvider amount, @Nullable Object operation, @Nullable Object equipmentSlotGroup) {
        this.key = key;
        this.amount = amount;
        this.operation = operation;
        this.equipmentSlotGroup = equipmentSlotGroup;
    }

    public AttributeModifier buildModifier(@NotNull Attribute attribute, @NotNull TextParserContext context){
        NamespacedKey k;
        if(key == null) k = Crux.key(attribute.key().value());
        else k = Crux.key(context.deserializeString(key.toString()));

        double a = amount.sample(context).doubleValue();//amount instanceof Number n ? n.doubleValue() : CruxMath.evaluate(context.deserializeString(amount.toString()));
        AttributeModifier.Operation o;
        String opString = operation == null ? null : operation.toString();
        if(operation == null) o = AttributeModifier.Operation.ADD_NUMBER;
        else if("add".equalsIgnoreCase(opString)){
          o = AttributeModifier.Operation.ADD_NUMBER;
        }else if("multiply".equalsIgnoreCase(opString)){
            o = AttributeModifier.Operation.MULTIPLY_SCALAR_1;
        }else{
            o = AttributeModifier.Operation.valueOf(context.deserializeString(operation.toString()).toUpperCase());
        }

        EquipmentSlotGroup slotGroup = equipmentSlotGroup == null ?
            EquipmentSlotGroup.ANY :
            EquipmentSlotGroup.getByName(context.deserializeString(equipmentSlotGroup.toString()));
        return new AttributeModifier(k, a, o, slotGroup);
    }

    public @Nullable Object getKey() {
        return key;
    }

    public @NotNull Object getAmount() {
        return amount;
    }

    public @Nullable Object getOperation() {
        return operation;
    }

    public @Nullable Object getEquipmentSlotGroup() {
        return equipmentSlotGroup;
    }
}
