package killercreepr.cruxpotion.data;

import killercreepr.crux.Crux;
import killercreepr.crux.data.entity.EntityDataHolder;
import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.cruxpotion.potions.ActivePotion;
import killercreepr.cruxpotion.potions.CustomPotion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;

/**
 * Represents an object/entity that can have custom potion effects applied to it.
 */
public class PotionHolder extends EntityDataHolder {
    public static final NamespacedKey KEY = Crux.key("potions");
    protected final Collection<ActivePotion> effects = new HashSet<>();

    public PotionHolder(@NotNull EntityMemory parent) {
        super(KEY, parent);
    }

    public @NotNull Collection<ActivePotion> getActiveEffects() { return effects; }

    public boolean addPotion(@NotNull ActivePotion potion){
        return addPotion(potion, false);
    }

    /**
     * @param potion The potion to add.
     * @param override Sets whether the potion should override the previous potion even if its amplifier is smaller.
     * @return Whether the potion was added.
     */
    public boolean addPotion(@NotNull ActivePotion potion, boolean override){
        boolean has = false;
        for(ActivePotion e : effects){
            if(!e.getPotion().getKey().equals(potion.getPotion().getKey())) continue;
            has = true;
            if((override || potion.getAmplifier() > e.getAmplifier() ||
                    potion.getAmplifier() == e.getAmplifier() && potion.compareDuration(e) > 0)){
                e.update(potion.getDuration(), potion.getAmplifier());
                return true;
            }
        }
        if(has) return false;
        effects.add(potion);
        potion.start();
        return true;
    }

    public void setPotions(@Nullable Collection<ActivePotion> effects){
        clearPotions();
        if(effects == null) return;
        for(ActivePotion p : effects){
            addPotion(p);
        }
    }

    /**
     * @return Whether the potion was removed.
     */
    public boolean removePotion(@NotNull CustomPotion type){
        return effects.removeIf(e ->{
            if(e.getPotion().getKey().equals(type.getKey())){
                e.stop();
                return true;
            }
            return false;
        });
    }

    /**
     * Clears all custom effects on this potion holder.
     * @return The amount of effects that were cleared.
     */
    public int clearPotions(){
        int size = effects.size();
        for(ActivePotion e : effects){
            e.stop();
        }
        effects.clear();
        return size;
    }

    /**
     * Stops all active effects without removing them.
     * You are most likely wanting to use clearPotions() instead!
     */
    public void stopPotions(){
        for(ActivePotion e : effects){
            e.stop();
        }
    }

    public int getPotionDuration(@NotNull CustomPotion type){
        ActivePotion p = getPotion(type);
        return p == null ? 0 : p.getDuration();
    }

    public int getPotionAmplifier(@NotNull CustomPotion type){
        ActivePotion p = getPotion(type);
        return p == null ? 0 : p.getAmplifier();
    }

    public @Nullable ActivePotion getPotion(@NotNull CustomPotion type){
        for(ActivePotion p : effects){
            if(p.getPotion().getKey().equals(type.getKey())) return p;
        }
        return null;
    }

    public boolean hasPotion(@NotNull CustomPotion type){
        return hasPotion(type.getKey());
    }

    public boolean hasPotion(@NotNull NamespacedKey key){
        for(ActivePotion p : effects){
            if(p.getPotion().getKey().equals(key)) return true;
        }
        return false;
    }

    @Override
    public void tick(@NotNull Entity entity) {
        for(ActivePotion a : new HashSet<>(effects)){
            if(a.tick()){
                effects.remove(a);
                a.stop();
            }
        }
        //if(entity instanceof Player p) p.sendPlayerListFooter(build());
    }

    public @NotNull Component build() {
        if(getActiveEffects().isEmpty()) return Component.empty();
        int index = 0;
        Component c = Component.empty();
        for(ActivePotion a : getActiveEffects()){
            index++;
            Component pot = a.buildTab();
            if(pot == null) continue;
            c = c.append(pot);
            if(index < getActiveEffects().size()){
                if(index % 3 == 0) c = c.append(Component.newline());
                else c = c.append(Component.text(" | ", NamedTextColor.DARK_GRAY));
            }
        }
        return c;
    }

    @Override
    public void removing() {
        clearPotions();
    }

    @Override
    public void parentRemoving() {
        clearPotions();
    }
}
