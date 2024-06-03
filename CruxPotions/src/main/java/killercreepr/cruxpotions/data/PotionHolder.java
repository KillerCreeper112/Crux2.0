package killercreepr.cruxpotions.data;

import killercreepr.crux.Crux;
import killercreepr.crux.data.entity.EntityDataHolder;
import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.cruxpotions.event.EntityCruxPotionEvent;
import killercreepr.cruxpotions.potions.ActivePotion;
import killercreepr.cruxpotions.potions.CruxPotion;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;

/**
 * Represents an object/entity that can have custom potion effects applied to it.
 */
public class PotionHolder extends EntityDataHolder {
    public static final Key KEY = Crux.key("potions");
    protected final Collection<ActivePotion> effects = new HashSet<>();

    public PotionHolder(@NotNull EntityMemory parent) {
        super(KEY, parent);
    }

    public @NotNull Collection<ActivePotion> getActiveEffects() { return effects; }

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
        ActivePotion has = null;
        for(ActivePotion e : effects){
            if(!e.getPotion().compare(potion.getPotion())) continue;
            has = e;
            if((override || potion.getAmplifier() > e.getAmplifier() ||
                    potion.getAmplifier() == e.getAmplifier() && potion.compareDuration(e) > 0)){

                EntityCruxPotionEvent event = new EntityCruxPotionEvent(
                        e.getEntity(), e, potion, EntityCruxPotionEvent.Action.CHANGED
                );
                if(!skipEventCall && !event.callEvent()) return event;

                e.update(potion.getDuration(), potion.getAmplifier());
                return event;
            }
        }
        if(has != null){
            EntityCruxPotionEvent event = new EntityCruxPotionEvent(
                    has.getEntity(), has, potion, EntityCruxPotionEvent.Action.NONE
            );
            if(!skipEventCall) event.callEvent();
            return event;
        }

        EntityCruxPotionEvent event = new EntityCruxPotionEvent(
                potion.getEntity(), potion, potion, EntityCruxPotionEvent.Action.ADDED
        );
        if(!skipEventCall && !event.callEvent()) return event;

        effects.add(potion);
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
        for(ActivePotion e : effects){
            if(e.getPotion().compare(type)){
                EntityCruxPotionEvent event = new EntityCruxPotionEvent(
                        e.getEntity(), e, null, EntityCruxPotionEvent.Action.REMOVED
                );
                if(!skipEventCall && !event.callEvent()) return event;
                e.stop();
                effects.remove(e);
                return event;
            }
        }
        return null;
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
        effects.removeIf(e ->{
            EntityCruxPotionEvent event = new EntityCruxPotionEvent(
                    e.getEntity(), e, null, EntityCruxPotionEvent.Action.CLEARED
            );
            list.add(event);
            if(!skipEventCall && !event.callEvent()) return false;
            e.stop();
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
        for(ActivePotion e : effects){
            e.stop();
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
        for(ActivePotion p : effects){
            if(p.getPotion().compare(type)) return p;
        }
        return null;
    }

    public boolean hasPotion(@NotNull CruxPotion type){
        return hasPotion(type.key());
    }

    public boolean hasPotion(@NotNull Key key){
        for(ActivePotion p : effects){
            if(p.getPotion().compare(key)) return true;
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
