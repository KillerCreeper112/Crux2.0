package killercreepr.cruxpotions.core.entity.memory;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.entity.memory.EntityTickedDataHolder;
import killercreepr.cruxpotions.api.entity.PotionHolder;
import killercreepr.cruxpotions.api.event.EntityCruxPotionEvent;
import killercreepr.cruxpotions.api.potion.ActivePotion;
import killercreepr.cruxpotions.api.potion.CruxPotion;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;

/**
 * Represents an object/entity that can have custom potion effects applied to it.
 */
public class SimplePotionHolder extends EntityTickedDataHolder implements PotionHolder {
    public static final Key KEY = Crux.key("potions");
    protected final Map<Key, ActivePotion> effects = new HashMap<>();

    public SimplePotionHolder(@NotNull EntityMemory parent) {
        super(KEY, parent);
    }

    public @NotNull Collection<ActivePotion> getActiveEffects() { return effects.values(); }

    public @NotNull EntityCruxPotionEvent addPotion(@NotNull ActivePotion potion){
        return addPotion(potion, false);
    }

    public @NotNull EntityCruxPotionEvent addPotion(@NotNull ActivePotion potion, boolean override){
        return addPotion(potion, override, false);
    }

    /**
     * @param potion The potion to add.
     * @param override Sets whether the potion should override the previous potion even if its amplifier is smaller.
     * @param skipEventCall If true, the EntityCruxPotionEvent will not be called and the effect will instantly be applied
     *                      if possible.
     * @return Whether the potion was added.
     */
    public @NotNull EntityCruxPotionEvent addPotion(@NotNull ActivePotion potion, boolean override, boolean skipEventCall){
        ActivePotion has = getPotion(potion.getPotion());
        if(has != null){
            if((override || potion.getAmplifier() > has.getAmplifier() ||
                potion.getAmplifier() == has.getAmplifier() && potion.compareDuration(has) > 0)){

                EntityCruxPotionEvent event = new EntityCruxPotionEvent(
                    getParent().value(), has, potion, EntityCruxPotionEvent.Action.CHANGED
                );
                if(!skipEventCall && !event.callEvent()) return event;

                has.update(potion.getDuration(), potion.getAmplifier());
                return event;
            }

            EntityCruxPotionEvent event = new EntityCruxPotionEvent(
                getParent().value(), has, potion, EntityCruxPotionEvent.Action.NONE
            );
            if(!skipEventCall) event.callEvent();
            return event;
        }

        EntityCruxPotionEvent event = new EntityCruxPotionEvent(
            getParent().value(), potion, potion, EntityCruxPotionEvent.Action.ADDED
        );
        if(!skipEventCall && !event.callEvent()) return event;

        effects.put(potion.getPotion().key(), potion);
        potion.start();
        return event;
    }

    public void setPotions(@Nullable Collection<ActivePotion> effects){
        clearPotions();
        if(effects == null) return;
        for(ActivePotion p : effects){
            addPotion(p);
        }
    }

    public boolean removePotionCheck(@NotNull CruxPotion type){
        EntityCruxPotionEvent e = removePotion(type);
        return e != null && !e.isCancelled();
    }

    /**
     * @return Null if no active potion of the specified type was found on this
     * data holder.
     */
    public @Nullable EntityCruxPotionEvent removePotion(@NotNull CruxPotion type){
        return removePotion(type, false);
    }

    /**
     * @return Null if no active potion of the specified type was found on this
     * data holder.
     */
    public @Nullable EntityCruxPotionEvent removePotion(@NotNull CruxPotion type, boolean skipEventCall){
        ActivePotion e = getPotion(type);
        if(e == null) return null;
        EntityCruxPotionEvent event = new EntityCruxPotionEvent(
            getParent().value(), e, null, EntityCruxPotionEvent.Action.REMOVED
        );
        if(!skipEventCall && !event.callEvent()) return event;

        try{
            e.stop();
        }catch (Exception ignored){
            Crux.log(Level.SEVERE, "EXCEPTION WHEN STOPPING ACTIVE POTION!");
            ignored.printStackTrace();
        }
        effects.remove(type.key());
        return event;
    }

    /**
     * Clears all custom effects on this potion holder.
     * @return The amount of effects that were cleared.
     */
    public int clearPotions(){
        Collection<EntityCruxPotionEvent> list = clearPotions(false);
        int amount = 0;
        for(EntityCruxPotionEvent e : list){
            if(!e.isCancelled()) amount++;
        }
        return amount;
    }

    public @NotNull Collection<EntityCruxPotionEvent> clearPotions(boolean skipEventCall){
        Collection<EntityCruxPotionEvent> list = new HashSet<>();
        effects.values().removeIf(e ->{
            EntityCruxPotionEvent event = new EntityCruxPotionEvent(
                getParent().value(), e, null, EntityCruxPotionEvent.Action.CLEARED
            );
            list.add(event);
            if(!skipEventCall && !event.callEvent()) return false;
            try{
                e.stop();
            }catch (Exception ignored){
                Crux.log(Level.SEVERE, "EXCEPTION WHEN STOPPING ACTIVE POTION!");
                ignored.printStackTrace();
            }
            return true;
        });
        return list;
    }

    /**
     * Stops all active effects without removing them.
     * You are most likely wanting to use clearPotions() instead!
     * <p>
     * This is primarily only used when a player quits the server.
     */
    public void stopPotions(){
        for(ActivePotion e : effects.values()){
            try{
                e.stop();
            }catch (Exception ignored){
                Crux.log(Level.SEVERE, "EXCEPTION WHEN STOPPING ACTIVE POTION!");
                ignored.printStackTrace();
            }
        }
    }

    public int getPotionDuration(@NotNull CruxPotion type){
        ActivePotion p = getPotion(type);
        return p == null ? 0 : p.getDuration();
    }

    public int getPotionAmplifier(@NotNull CruxPotion type){
        ActivePotion p = getPotion(type);
        return p == null ? 0 : p.getAmplifier();
    }

    public @Nullable ActivePotion getPotion(@NotNull CruxPotion type){
        return effects.get(type.key());
    }

    public boolean hasPotion(@NotNull CruxPotion type){
        return hasPotion(type.key());
    }

    public boolean hasPotion(@NotNull Key key){
       return effects.containsKey(key);
    }

    @Override
    public void tick(@NotNull Entity entity) {
        effects.values().removeIf(a ->{
            try{
                if(a.tick()){
                    a.stop();
                    return true;
                }
            }catch (Exception e){
                Crux.log(Level.SEVERE, "EXCEPTION WHEN TICKING ACTIVE POTION!");
                e.printStackTrace();
                return true;
            }
            return false;
        });
        //if(entity instanceof Player p) p.sendPlayerListFooter(build());
    }

    public @NotNull Component build() {
        if(getActiveEffects().isEmpty()) return Component.empty();
        int index = 0;
        Component c = Component.empty();
        for(ActivePotion a : getActiveEffects()){
            index++;
            Component pot = a.format();
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
    public void removing(@Nullable Entity e) {
        super.removing(e);
        clearPotions();
    }

    @Override
    public void parentRemoving(@Nullable Entity e) {
        clearPotions();
    }
}
