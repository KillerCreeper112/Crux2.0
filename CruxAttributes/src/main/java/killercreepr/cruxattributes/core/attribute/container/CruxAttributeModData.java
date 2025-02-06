package killercreepr.cruxattributes.core.attribute.container;

import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.api.attribute.CruxAttributeModifier;
import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxAttributeModData {
    private final double amount;
    private final CruxAttribute.Operation operation;
    private final CruxSlotGroup slot;

    public CruxAttributeModData(@NotNull CruxAttributeModifier m){
        this.amount = m.getAmount();
        this.operation = m.getOperation();
        this.slot = m.getSlotGroup();
    }

    public CruxAttributeModData(double amount, @NotNull CruxAttribute.Operation operation, @Nullable CruxSlotGroup slot) {
        this.amount = amount;
        this.operation = operation;
        this.slot = slot;
    }

    public double getAmount() {
        return amount;
    }

    public @NotNull CruxAttribute.Operation getOperation() {
        return operation;
    }

    public @Nullable CruxSlotGroup getSlotGroup() {
        return slot;
    }
}
