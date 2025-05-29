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
    private final double amount;
    private final CruxAttribute.Operation operation;
    private final CruxSlotGroup slot;
    private final Key[] path;

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
        this.path = null;
    }

    public SimpleCruxAttributeModifier(@NotNull Key key, double amount, @NotNull CruxAttribute.Operation operation,
                                       @Nullable CruxSlotGroup slot, Key... path) {
        this.key = key;
        this.amount = amount;
        this.operation = operation;
        this.slot = slot;
        this.path = path;
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
        return new SimpleCruxAttributeModifier(newKey, amount, operation, slot, copyPath());
    }

    public @NotNull Key@Nullable[] getPath() {
        return path;
    }

    @Override
    public CruxAttributeModifier withPath(@NotNull Key @Nullable ... path) {
        return new SimpleCruxAttributeModifier(
            key, amount, operation, slot, (path == null || path.length < 1) ? null : path
        );
    }

    private Key[] copyPath(){
        return path == null ? null : Arrays.copyOf(path, path.length);
    }

    @Override
    public CruxAttributeModifier withAmount(double amount) {
        return new SimpleCruxAttributeModifier(
            key, amount, operation, slot, copyPath()
        );
    }

    @Override
    public CruxAttributeModifier withOperation(CruxAttribute.@NotNull Operation operation) {
        return new SimpleCruxAttributeModifier(
            key, amount, operation, slot, copyPath()
        );
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
        this.path = null;
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
    public @NotNull CruxAttributeModifier copy() {
        return withKey(key);
    }

    @Override
    public String toString() {
        return "SimpleCruxAttributeModifier{key=" + key + ", amount=" + amount + ", operation=" + operation + ", slot=" + slot + ", path=" + Arrays.toString(path) + "}";
    }
}
