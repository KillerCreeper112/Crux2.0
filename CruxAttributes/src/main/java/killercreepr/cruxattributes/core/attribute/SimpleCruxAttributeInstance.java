package killercreepr.cruxattributes.core.attribute;

import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.api.attribute.CruxAttributeInstance;
import killercreepr.cruxattributes.api.attribute.CruxAttributeModifier;
import killercreepr.cruxattributes.api.attribute.DynamicCruxAttributeInstance;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;


public class SimpleCruxAttributeInstance implements CruxAttributeInstance {
    /*public static boolean matchesPath(@NotNull CruxAttributeModifier modifier, @NotNull Key... path){
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
    }*/
    public static boolean matchesPath(@NotNull CruxAttributeModifier modifier, @NotNull Key... path) {
        if (path.length == 0) return false;

        Key[] modPath = modifier.getPath(); // May be null
        Key modKey = modifier.key();

        // Case: modifier has no path, just a key
        if (modPath == null) {
            return path.length == 1 && modKey.equals(path[0]);
        }

        // Full modifier path = modPath + modKey
        int fullLength = modPath.length + 1;
        if (path.length > fullLength) return false;

        for (int i = 0; i < path.length; i++) {
            if (i < modPath.length) {
                if (!modPath[i].equals(path[i])) return false;
            } else {
                // Compare to modKey
                if (!modKey.equals(path[i])) return false;
            }
        }

        return true;
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

    protected double cachedValue;
    protected boolean dirty = true;
    public double getValue(){
        if(!dirty) return cachedValue;
        dirty = false;
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
        return cachedValue = attribute.processValue(x); //attribute.equals(CruxAttribute.ATTACK_SPEED) ? x*-1D : x;
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
            return dirty = modifiers.removeIf(m -> SimpleCruxAttributeInstance.matchesPath(m, path));
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
            dirty = true;

            /*for(CruxAttributeModifier mod : modifiers){
                this.modifiers.removeIf(compare -> compare.key().equals(mod.key()) && Arrays.equals(compare.getPath(), mod.getPath()));
                this.modifiers.add(mod);
            }*/
        }

        @Override
        public @NotNull DynamicCruxAttributeInstance copy() {
            Collection<CruxAttributeModifier> list = new ArrayList<>(modifiers);
            var copy = new SimpleCruxAttributeInstance.Dynamic(attribute, list);
            copy.dirty = dirty;
            copy.cachedValue = cachedValue;
            return copy;
        }
    }
}
