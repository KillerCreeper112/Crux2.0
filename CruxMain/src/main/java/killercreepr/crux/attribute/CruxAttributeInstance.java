package killercreepr.crux.attribute;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CruxAttributeInstance {
    private final CruxAttribute attribute;
    private final Collection<CruxAttributeModifier> modifiers = new ArrayList<>();
    public CruxAttributeInstance(@NotNull CruxAttribute attribute, @NotNull Collection<CruxAttributeModifier> modifiers) {
        this.attribute = attribute;
        this.modifiers.addAll(modifiers);
    }

    public CruxAttributeInstance(@NotNull CruxAttribute attribute, @NotNull CruxAttributeModifier... modifiers) {
        this.attribute = attribute;
        this.modifiers.addAll(List.of(modifiers));
    }

    public double getValue(@NotNull CruxAttribute.Operation operation){
        double x = 0D;
        for(CruxAttributeModifier m : modifiers){
            if(m.getOperation() == operation) x += m.getAmount();
        }
        return x;
    }

    public double getValue(){
        double x = 0D;
        double multiply = 0D;
        Collection<CruxAttributeModifier> ADD = new ArrayList<>();
        Set<NamespacedKey> SET = new HashSet<>();
        for(CruxAttributeModifier m : modifiers){
            switch (m.getOperation()){
                case MULTIPLY -> multiply += m.getAmount();
                case SET ->{
                    SET.add(m.getKey());
                    x = m.getAmount();
                }
                case ADD -> ADD.add(m);
            }
        }
        for(CruxAttributeModifier m : ADD){
            if(SET.contains(m.getKey())) continue;
            x += m.getAmount();
        }
        if(multiply != 0D) x *= (1D + multiply);
        return attribute.processValue(x); //attribute.equals(CruxAttribute.ATTACK_SPEED) ? x*-1D : x;
    }

    public double getBaseValue(){
        for(CruxAttributeModifier m : modifiers){
            if(m.isBase()) return m.getAmount();
        }
        return 0D;
    }

    public @NotNull CruxAttribute getAttribute() {
        return attribute;
    }

    public @Nullable CruxAttributeModifier getModifier(@NotNull NamespacedKey key){
        for(CruxAttributeModifier m : modifiers){
            if(m.getKey().equals(key)) return m;
        }
        return null;
    }

    public @NotNull Collection<CruxAttributeModifier> getModifiers() {
        return modifiers;
    }
}
