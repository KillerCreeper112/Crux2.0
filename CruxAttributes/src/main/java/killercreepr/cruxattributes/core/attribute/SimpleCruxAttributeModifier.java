package killercreepr.cruxattributes.core.attribute;

import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.api.attribute.CruxAttributeModifier;
import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import killercreepr.cruxattributes.core.attribute.container.CruxAttributeModData;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class SimpleCruxAttributeModifier implements CruxAttributeModifier {
    private final Key key;
    private double amount;
    private final CruxAttribute.Operation operation;
    private final CruxSlotGroup slot;
    private Key[] path;

    public SimpleCruxAttributeModifier(@NotNull Key key, @NotNull CruxAttributeModData data) {
        this(key, data.getAmount(), data.getOperation(), data.getSlotGroup());
    }

    public SimpleCruxAttributeModifier(@NotNull Key key, double amount) {
        this(key, amount, CruxAttribute.Operation.ADD, null);
    }

    public SimpleCruxAttributeModifier(@NotNull Key key, double amount, @NotNull CruxAttribute.Operation operation) {
        this(key, amount, operation, null);
    }

    public SimpleCruxAttributeModifier(@NotNull Key key, double amount, @NotNull CruxAttribute.Operation operation,
                                       @Nullable CruxSlotGroup slot) {
        this.key = key;
        this.amount = amount;
        this.operation = operation;
        this.slot = slot;
    }

    public SimpleCruxAttributeModifier(@NotNull Key key, double amount,
                                       @Nullable CruxSlotGroup slot) {
        this(key, amount, CruxAttribute.Operation.ADD, slot);
    }

    public SimpleCruxAttributeModifier(double amount,
                                       @NotNull CruxAttribute.Operation operation) {
        this(amount, operation, null);
    }

    public SimpleCruxAttributeModifier withKey(@NotNull Key newKey){
        return new SimpleCruxAttributeModifier(newKey, amount, operation, slot);
    }

    public @NotNull Key@Nullable[] getPath() {
        return path;
    }

    public SimpleCruxAttributeModifier setPath(@NotNull Key @Nullable... path){
        this.path = path;
        return this;
    }

    /**
     * Creates a base attribute modifier.
     */
    public SimpleCruxAttributeModifier(double amount) {
        this(amount, CruxAttribute.Operation.ADD, null);
    }

    /**
     * Creates a base attribute modifier.
     */
    public SimpleCruxAttributeModifier(double amount, @Nullable CruxSlotGroup slot) {
        this(amount, CruxAttribute.Operation.ADD, slot);
    }

    /**
     * Creates a base attribute modifier.
     */
    public SimpleCruxAttributeModifier(double amount, @NotNull CruxAttribute.Operation operation, @Nullable CruxSlotGroup slot) {
        this.key = CruxAttribute.k("base");
        this.amount = amount;
        this.operation = operation;
        this.slot = slot;
    }

    public SimpleCruxAttributeModifier setAmount(double amount) {
        this.amount = amount; return this;
    }

    public boolean isBase(){
        return key.equals(CruxAttribute.k("base"));
    }

    @Override
    public @NotNull Key key() {
        return key;
    }


    public double getAmount() {
        return amount;
    }

    public @NotNull CruxAttribute.Operation getOperation() {
        return operation;
    }

    @NotNull
    public CruxSlotGroup getSlotGroup() {
        if(slot == null) return CruxSlotGroup.ANY;
        return slot;
    }

    @Override
    public String toString() {
        return "SimpleCruxAttributeModifier{key=" + key + ", amount=" + amount + ", operation=" + operation + ", slot=" + slot + ", path=" + Arrays.toString(path) + "}";
    }
}
