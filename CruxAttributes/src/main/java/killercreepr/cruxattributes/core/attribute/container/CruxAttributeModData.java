package killercreepr.cruxattributes.core.attribute.container;

import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.core.attribute.CruxAttributeModifier;
import killercreepr.cruxattributes.api.attribute.CruxSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxAttributeModData {
    private final double amount;
    private final CruxAttribute.Operation operation;
    private final CruxSlot slot;

    public CruxAttributeModData(@NotNull CruxAttributeModifier m){
        this.amount = m.getAmount();
        this.operation = m.getOperation();
        this.slot = m.getSlot();
    }

    public CruxAttributeModData(double amount, @NotNull CruxAttribute.Operation operation, @Nullable CruxSlot slot) {
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

    public @Nullable CruxSlot getSlot() {
        return slot;
    }
}
