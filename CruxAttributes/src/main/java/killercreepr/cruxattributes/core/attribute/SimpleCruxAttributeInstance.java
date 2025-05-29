package killercreepr.cruxattributes.core.attribute;

import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.api.attribute.CruxAttributeInstance;
import killercreepr.cruxattributes.api.attribute.CruxAttributeModifier;
import killercreepr.cruxattributes.api.attribute.DynamicCruxAttributeInstance;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;


public class SimpleCruxAttributeInstance implements CruxAttributeInstance {
    public static boolean matchesPath(@NotNull CruxAttributeModifier modifier, @NotNull Key... path){
        int lengthOne = path.length-1;
        Key key = path.length > 1 ? null : path[lengthOne];
        Key[] modPath = modifier.getPath();
        if(modPath == null){
            if(key == null) return false;
            return key.equals(modifier.key());
        }else if(key != null) return false;

        if (modPath.length < (lengthOne)) return false;
        for (int i = 0; i < lengthOne; i++) {
            if (!modPath[i].equals(path[i])) {
                return false;
            }
        }
        if(modPath.length < path.length) return modifier.key().equals(path[lengthOne]);
        return modPath[lengthOne].equals(path[lengthOne]);
    }
    protected final CruxAttribute attribute;
    protected final Collection<CruxAttributeModifier> modifiers = new ArrayList<>();
    public SimpleCruxAttributeInstance(@NotNull CruxAttribute attribute, @NotNull Collection<CruxAttributeModifier> modifiers) {
        this.attribute = attribute;
        this.modifiers.addAll(modifiers);
    }

    public SimpleCruxAttributeInstance(@NotNull CruxAttribute attribute, @NotNull CruxAttributeModifier... modifiers) {
        this.attribute = attribute;
        if(modifiers != null && modifiers.length > 0) this.modifiers.addAll(List.of(modifiers));
    }

    @Override
    public String toString() {
        return "SimpleCruxAttributeInstance{attribute=" + attribute + ", modifiers=" + modifiers + "}";
    }

    @Override
    public boolean isEmpty() {
        return modifiers.isEmpty();
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

    public @Nullable CruxAttributeModifier getModifier(@NotNull Key key){
        for(CruxAttributeModifier m : modifiers){
            if(m.key().equals(key)) return m;
        }
        return null;
    }

    @Override
    public @NotNull List<CruxAttributeModifier> getModifiers(@NotNull CruxAttribute attribute, @NotNull Key... path) {
        List<CruxAttributeModifier> list = new ArrayList<>();
        for(CruxAttributeModifier m : modifiers){
            if(matchesPath(m, path)) list.add(m);
        }
        return list;
    }

    public @NotNull Collection<CruxAttributeModifier> getModifiers() {
        return modifiers;
    }

    public static class Dynamic extends SimpleCruxAttributeInstance implements DynamicCruxAttributeInstance {

        public Dynamic(@NotNull CruxAttribute attribute, @NotNull Collection<CruxAttributeModifier> modifiers) {
            super(attribute, modifiers);
        }

        public Dynamic(@NotNull CruxAttribute attribute, @NotNull CruxAttributeModifier... modifiers) {
            super(attribute, modifiers);
        }

        @Override
        public boolean removeModifiers(@NotNull Key... path) {
            return modifiers.removeIf(m -> SimpleCruxAttributeInstance.matchesPath(m, path));
        }

        @Override
        public void addModifiers(@NotNull CruxAttributeModifier... modifiers) {
            Set<CruxAttributeModifier> toRemove = new HashSet<>();

            for (CruxAttributeModifier mod : modifiers) {
                for (CruxAttributeModifier existing : this.modifiers) {
                    if (existing.key().equals(mod.key()) && Arrays.equals(existing.getPath(), mod.getPath())) {
                        toRemove.add(existing);
                    }
                }
            }

            this.modifiers.removeAll(toRemove);
            Collections.addAll(this.modifiers, modifiers);

            /*for(CruxAttributeModifier mod : modifiers){
                this.modifiers.removeIf(compare -> compare.key().equals(mod.key()) && Arrays.equals(compare.getPath(), mod.getPath()));
                this.modifiers.add(mod);
            }*/
        }

        @Override
        public @NotNull DynamicCruxAttributeInstance copy() {
            Collection<CruxAttributeModifier> list = new ArrayList<>(modifiers);
            return new SimpleCruxAttributeInstance.Dynamic(attribute, list);
        }
    }
}
