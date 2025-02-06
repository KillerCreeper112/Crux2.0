package killercreepr.cruxattributes.core.attribute;

import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.api.attribute.CruxAttributeInstance;
import killercreepr.cruxattributes.api.attribute.CruxAttributeModifier;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;


public class SimpleCruxAttributeInstance implements CruxAttributeInstance {
    private final CruxAttribute attribute;
    private final Collection<CruxAttributeModifier> modifiers = new ArrayList<>();
    public SimpleCruxAttributeInstance(@NotNull CruxAttribute attribute, @NotNull Collection<CruxAttributeModifier> modifiers) {
        this.attribute = attribute;
        this.modifiers.addAll(modifiers);
    }

    public SimpleCruxAttributeInstance(@NotNull CruxAttribute attribute, @NotNull CruxAttributeModifier... modifiers) {
        this.attribute = attribute;
        this.modifiers.addAll(List.of(modifiers));
    }

    @Override
    public String toString() {
        return "SimpleCruxAttributeInstance{attribute=" + attribute + ", modifiers=" + modifiers + "}";
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
        Set<Key> SET = new HashSet<>();
        for(CruxAttributeModifier m : modifiers){
            switch (m.getOperation()){
                case CruxAttribute.Operation.MULTIPLY -> multiply += m.getAmount();
                case CruxAttribute.Operation.SET ->{
                    SET.add(m.key());
                    x = m.getAmount();
                }
                case CruxAttribute.Operation.ADD -> ADD.add(m);
            }
        }
        for(CruxAttributeModifier m : ADD){
            if(SET.contains(m.key())) continue;
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
            if(m.key().equals(key)) return m;
        }
        return null;
    }

    public @NotNull Collection<CruxAttributeModifier> getModifiers() {
        return modifiers;
    }
}
