package killercreepr.crux.attribute;

import killercreepr.crux.attribute.container.CruxAttributeModData;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxAttributeModifier implements Keyed {
    private final NamespacedKey key;
    private double amount;
    private final CruxAttribute.Operation operation;
    private final CruxSlot slot;
    private NamespacedKey[] path;

    public CruxAttributeModifier(@NotNull NamespacedKey key, @NotNull CruxAttributeModData data) {
        this.key = key;
        this.amount = data.getAmount();
        this.operation = data.getOperation();
        this.slot = data.getSlot();
    }

    public CruxAttributeModifier(@NotNull NamespacedKey key, double amount) {
        this.key = key;
        this.amount = amount;
        this.operation = CruxAttribute.Operation.ADD;
        this.slot = null;
    }

    public CruxAttributeModifier(@NotNull NamespacedKey key, double amount, @NotNull CruxAttribute.Operation operation) {
        this.key = key;
        this.amount = amount;
        this.operation = operation;
        this.slot = null;
    }

    public CruxAttributeModifier(@NotNull NamespacedKey key, double amount, @NotNull CruxAttribute.Operation operation,
                                 @Nullable CruxSlot slot) {
        this.key = key;
        this.amount = amount;
        this.operation = operation;
        this.slot = slot;
    }

    public CruxAttributeModifier(@NotNull NamespacedKey key, double amount,
                                 @Nullable CruxSlot slot) {
        this.key = key;
        this.amount = amount;
        this.operation = CruxAttribute.Operation.ADD;
        this.slot = slot;
    }

    public CruxAttributeModifier withKey(@NotNull NamespacedKey newKey){
        return new CruxAttributeModifier(newKey, amount, operation, slot);
    }

    public @NotNull NamespacedKey@Nullable[] getPath() {
        return path;
    }

    public CruxAttributeModifier setPath(@NotNull NamespacedKey @Nullable... path){
        this.path = path;
        return this;
    }

    /**
     * Creates a base attribute modifier.
     */
    public CruxAttributeModifier(double amount) {
        this(amount, CruxAttribute.Operation.ADD, null);
    }

    /**
     * Creates a base attribute modifier.
     */
    public CruxAttributeModifier(double amount, @Nullable CruxSlot slot) {
        this(amount, CruxAttribute.Operation.ADD, slot);
    }

    /**
     * Creates a base attribute modifier.
     */
    public CruxAttributeModifier(double amount, @NotNull CruxAttribute.Operation operation, @Nullable CruxSlot slot) {
        this.key = CruxAttribute.k("base");
        this.amount = amount;
        this.operation = operation;
        this.slot = slot;
    }

    public CruxAttributeModifier setAmount(double amount) {
        this.amount = amount; return this;
    }

    public boolean isBase(){
        return key.equals(CruxAttribute.k("base"));
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    public @NotNull NamespacedKey getKey() {
        return key;
    }

    public double getAmount() {
        return amount;
    }

    public @NotNull CruxAttribute.Operation getOperation() {
        return operation;
    }

    @Nullable
    public CruxSlot getSlot() {
        return slot;
    }
}
