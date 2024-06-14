package killercreepr.cruxpotions.potions;

import killercreepr.crux.Crux;
import killercreepr.cruxpotions.registries.CruxPotionRegistries;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class ActivePotionImpl implements ActivePotion, Listener, ConfigurationSerializable {
    protected final CruxPotion potion;
    protected final Entity entity;
    protected int duration;
    protected int amplifier;
    protected boolean stop = true;
    public ActivePotionImpl(@NotNull CruxPotion potion, @NotNull Entity entity, int duration, int amplifier) {
        this.potion = potion;
        this.entity = entity;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    @Override
    public final int compareDuration(@NotNull ActivePotion check) {
        return ActivePotion.super.compareDuration(check);
    }

    @Override
    public final boolean hasInfiniteDuration(){
        return ActivePotion.super.hasInfiniteDuration();
    }

    @Override
    public @Nullable Component format(){
        return Crux.FORMAT.deserialize("<gray>" + ActivePotion.formatPotion(potion, amplifier, duration));
    }

    /**
     * @return Whether to remove this potion from the entity.
     */
    @Override
    public final boolean tick(){
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
    public final void update(int newDuration, int newAmplifier){
        int oldDuration = this.duration;
        int oldAmplifier = this.amplifier;
        this.duration = newDuration;
        this.amplifier = newAmplifier;
        onUpdate(oldDuration, oldAmplifier);
    }

    @Override
    public ActivePotionImpl setDuration(int duration, boolean silent) {
        if(silent) this.duration = duration;
        else update(duration, amplifier);
        return this;
    }

    @Override
    public ActivePotionImpl setAmplifier(int amplifier, boolean silent) {
        if(silent) this.amplifier = amplifier;
        else update(duration, amplifier);
        return this;
    }

    /**
     * Called when the active effect has essentially been re-applied with potentially a different duration and/or amplifier.
     */
    protected void onUpdate(int oldDuration, int oldAmplifier){}

    @Override
    public final void start(){
        stop = false;
        Bukkit.getPluginManager().registerEvents(this, Crux.getMainPlugin());
        onStart();
    }

    /**
     * Called when the effect has just been applied and activated on an entity.
     */
    protected void onStart(){}
    @Override
    public final void stop(){
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
    @Override
    public @NotNull Entity getEntity() {
        return entity;
    }
    @Override
    public final int getDuration() {
        return duration;
    }
    @Override
    public final int getAmplifier() {
        return amplifier;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        final LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("entity", entity.getUniqueId().toString());
        result.put("type", potion.key().asString());
        result.put("amplifier", amplifier);
        result.put("duration", duration);
        return result;
    }
    public static @NotNull ActivePotion deserialize(@NotNull Map<String, Object> args){
        final Key key = Crux.key((String) args.getOrDefault("type", "a"));
        final CruxPotion p = CruxPotionRegistries.POTIONS.get(key);
        if(p == null){
            throw new RuntimeException("Custom potion of '" + key + "' cannot be found!");
        }
        Entity e;
        try{
            e = Bukkit.getEntity(UUID.fromString((String) args.getOrDefault("entity", "")));
        }catch (IllegalArgumentException ignored){
            e = null;
        }
        if(e == null){
            throw new RuntimeException("Entity of '" + args.getOrDefault("entity", "") + "' cannot be found for potion: " + key + "!");
        }
        final int amplifier = (int) args.getOrDefault("amplifier", 0);
        final int duration = (int) args.getOrDefault("duration", 0);
        return p.create(e, duration, amplifier);
    }
}
