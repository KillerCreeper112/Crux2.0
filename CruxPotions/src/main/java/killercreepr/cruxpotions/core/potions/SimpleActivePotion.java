package killercreepr.cruxpotions.core.potions;

import killercreepr.crux.core.Crux;
import killercreepr.cruxpotions.api.potion.ActivePotion;
import killercreepr.cruxpotions.api.potion.CruxPotion;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleActivePotion implements ActivePotion, Listener {
    protected final CruxPotion potion;
    protected final Entity entity;
    protected int duration;
    protected int amplifier;
    protected boolean stop = true;
    public SimpleActivePotion(@NotNull CruxPotion potion, @NotNull Entity entity, int duration, int amplifier) {
        this.potion = potion;
        this.entity = entity;
        this.duration = duration;
        this.amplifier = amplifier;
    }
    @Override
    public @Nullable Component format(){
        return Crux.format().deserialize("<gray>" + ActivePotion.formatPotion(potion, amplifier, duration));
    }

    /**
     * @return Whether to remove this potion from the entity.
     */
    @Override
    public boolean tick(){
        if((duration != -1 && duration-- < 1) || stop){
            return true;
        }
        onTick();
        return false;
    }

    /**
     * Called every tick that the potion is active on an entity.
     */
    protected void onTick(){}

    @Override
    public void update(int newDuration, int newAmplifier){
        int oldDuration = this.duration;
        int oldAmplifier = this.amplifier;
        this.duration = newDuration;
        this.amplifier = newAmplifier;
        onUpdate(oldDuration, oldAmplifier);
    }

    @Override
    public SimpleActivePotion setDuration(int duration, boolean silent) {
        if(silent) this.duration = duration;
        else update(duration, amplifier);
        return this;
    }

    @Override
    public SimpleActivePotion setAmplifier(int amplifier, boolean silent) {
        if(silent) this.amplifier = amplifier;
        else update(duration, amplifier);
        return this;
    }

    /**
     * Called when the active effect has essentially been re-applied with potentially a different duration and/or amplifier.
     */
    protected void onUpdate(int oldDuration, int oldAmplifier){}

    @Override
    public void start(){
        stop = false;
        Bukkit.getPluginManager().registerEvents(this, Crux.getMainPlugin());
        onStart();
    }

    /**
     * Called when the effect has just been applied and activated on an entity.
     */
    protected void onStart(){}
    @Override
    public void stop(){
        stop = true;
        HandlerList.unregisterAll(this);
        onStop();
    }

    /**
     * Called when the effect has been removed from an entity.
     */
    protected void onStop(){}
    @Override
    public @NotNull CruxPotion getPotion() {
        return potion;
    }

    public @NotNull Entity getEntity() {
        return entity;
    }
    @Override
    public int getDuration() {
        return duration;
    }
    @Override
    public int getAmplifier() {
        return amplifier;
    }
}
