package killercreepr.cruxpotion.potions;

import killercreepr.crux.Crux;
import killercreepr.crux.util.CruxMath;
import killercreepr.cruxpotion.registries.CruxPotionRegistries;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang.StringUtils;
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

public class ActivePotion implements Listener, ConfigurationSerializable {
    protected final CruxPotion potion;
    protected final Entity entity;
    protected int duration;
    protected int amplifier;
    protected boolean stop = true;
    public ActivePotion(@NotNull CruxPotion potion, @NotNull Entity entity, int duration, int amplifier) {
        this.potion = potion;
        this.entity = entity;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    /**
     * @return 0 if check duration is equal to duration,
     * 1 if duration > than check duration
     * -1 if duration < than check duration
     */
    public final int compareDuration(@NotNull ActivePotion check){
        if(duration == check.getDuration()) return 0;
        if(infiniteDuration()) return 1;
        if(check.infiniteDuration()) return -1;
        return Integer.compare(duration, check.duration);
    }

    /**
     * @return Whether this effect is infinite.
     */
    public final boolean infiniteDuration(){ return duration == -1; }

    public @Nullable Component buildTab(){
        Component c = Component.text(potion.getName()).append(Component.space());
        if(amplifier > 0) c = c.append(Component.text(CruxMath.numeral(amplifier+1))).append(Component.space());
        if(duration == -1) c = c.append(Component.text("∞"));
        else{
            int seconds = duration / 20;
            int minutes = seconds / 60;
            seconds -= minutes * 60;
            c = c.append(Component.text(minutes + ":" + CruxMath.padWithZeroIfSingleDigit(seconds), NamedTextColor.GRAY));
        }
        return c;
    }

    public double evaluate(@Nullable String eq) {
        return eq == null ? 0D : CruxMath.evaluate(
                StringUtils.replace(
                        StringUtils.replace(eq, "%amp%", String.valueOf(amplifier)),
                        "%dur%", String.valueOf(duration)
                )
        );
    }

    /**
     * @return Whether to remove this potion from the entity.
     */
    public final boolean tick(){
        if((duration != -1 && duration-- < 1) || stop){
            return true;
        }
        t();
        return false;
    }

    /**
     * Called every tick that the potion is active on an entity.
     */
    protected void t(){}

    /**
     * @param newDuration The new duration (in ticks) that this active effect should have.
     * @param newAmplifier The new amplifier that this active effect should have.
     */
    public final void update(int newDuration, int newAmplifier){
        int oldDuration = this.duration;
        int oldAmplifier = this.amplifier;
        this.duration = newDuration;
        this.amplifier = newAmplifier;
        updated(oldDuration, oldAmplifier);
    }

    public ActivePotion setDuration(int duration) {
        return setDuration(duration, false);
    }

    public ActivePotion setAmplifier(int amplifier) {
        return setAmplifier(amplifier, false);
    }

    /**
     *
     * @param duration The new effect's durations.
     * @param silent If this is false, the potion's update function will not be called.
     * @return The same active potion, for chaining.
     */
    public ActivePotion setDuration(int duration, boolean silent) {
        if(silent) this.duration = duration;
        else update(duration, amplifier);
        return this;
    }

    /**
     *
     * @param amplifier The new effect's amplifier.
     * @param silent If this is true, the potion's update function will not be called.
     * @return The same active potion, for chaining.
     */
    public ActivePotion setAmplifier(int amplifier, boolean silent) {
        if(silent) this.amplifier = amplifier;
        else update(duration, amplifier);
        return this;
    }

    /**
     * Called when the active effect has essentially been re-applied with potentially a different duration and/or amplifier.
     */
    protected void updated(int oldDuration, int oldAmplifier){}

    public final void start(){
        stop = false;
        Bukkit.getPluginManager().registerEvents(this, Crux.getMainPlugin());
        started();
    }

    /**
     * Called when the effect has just been applied and activated on an entity.
     */
    protected void started(){}

    public final void stop(){
        stop = true;
        HandlerList.unregisterAll(this);
        stopped();
    }

    /**
     * Called when the effect has been removed from an entity.
     */
    protected void stopped(){}

    public @NotNull CruxPotion getPotion() {
        return potion;
    }

    public @NotNull Entity getEntity() {
        return entity;
    }

    public final int getDuration() {
        return duration;
    }

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
