package killercreepr.cruxattributes.api.attribute;

import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import killercreepr.cruxattributes.core.attribute.SimpleCruxAttributeModifier;
import killercreepr.cruxattributes.core.attribute.container.CruxAttributeModData;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CruxAttributeModifier extends Keyed {
    static CruxAttributeModifier modifier(@NotNull Key key, double amount, @NotNull CruxAttribute.Operation operation, @Nullable CruxSlotGroup slot){
        return new SimpleCruxAttributeModifier(key, amount, operation, slot);
    }

    static CruxAttributeModifier modifier(@NotNull Key key, @NotNull CruxAttributeModData data){
        return new SimpleCruxAttributeModifier(key, data);
    }

    static CruxAttributeModifier modifier(@NotNull Key key, double amount, @NotNull CruxAttribute.Operation operation){
        return modifier(key, amount, operation, null);
    }

    static CruxAttributeModifier modifier(@NotNull Key key, double amount, @Nullable CruxSlotGroup slot){
        return new SimpleCruxAttributeModifier(key, amount, slot);
    }

    static CruxAttributeModifier modifier(@NotNull Key key, double amount){
        return new SimpleCruxAttributeModifier(key, amount);
    }

    static CruxAttributeModifier baseModifier(double amount, @NotNull CruxAttribute.Operation operation, @Nullable CruxSlotGroup slot){
        return new SimpleCruxAttributeModifier(amount, operation, slot);
    }

    static CruxAttributeModifier baseModifier(double amount, @NotNull CruxAttribute.Operation operation){
        return new SimpleCruxAttributeModifier(amount, operation);
    }

    static CruxAttributeModifier baseModifier(double amount, @Nullable CruxSlotGroup slot){
        return new SimpleCruxAttributeModifier(amount, slot);
    }

    static CruxAttributeModifier baseModifier(double amount){
        return new SimpleCruxAttributeModifier(amount);
    }

    static CruxAttributeModifier addModifier(@NotNull Key key, double amount, @Nullable CruxSlotGroup slot){
        return modifier(key, amount, CruxAttribute.Operation.ADD, slot);
    }

    static CruxAttributeModifier addModifier(@NotNull Key key, double amount){
        return addModifier(key, amount, null);
    }

    CruxAttributeModifier withKey(@NotNull Key newKey);
    @NotNull Key@Nullable[] getPath();
    CruxAttributeModifier setPath(@NotNull Key @Nullable... path);

    CruxAttributeModifier setAmount(double amount);

    default boolean isBase(){
        return key().equals(CruxAttribute.k("base"));
    }

    double getAmount();

    @NotNull CruxAttribute.Operation getOperation();

    @NotNull
    CruxSlotGroup getSlotGroup();
}
