package killercreepr.cruxpotions.potions;

import killercreepr.cruxpotions.potions.inflictor.PotionInflictor;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class InflictedPotion extends ActivePotion{
    protected PotionInflictor inflictor;
    public InflictedPotion(@NotNull CruxPotion potion, @NotNull Entity entity, int duration, int amplifier) {
        super(potion, entity, duration, amplifier);
    }

    public InflictedPotion(@NotNull CruxPotion potion, @NotNull Entity entity, int duration, int amplifier,
                           @Nullable PotionInflictor inflictor) {
        super(potion, entity, duration, amplifier);
        this.inflictor = inflictor;
    }

    public @Nullable PotionInflictor getInflictor() {
        return inflictor;
    }

    public InflictedPotion setInflictor(@Nullable PotionInflictor inflictor) {
        this.inflictor = inflictor;
        return this;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        final Map<String, Object> result = super.serialize();
        result.put("inflictor", inflictor);
        return result;
    }
    public static @NotNull ActivePotion deserialize(@NotNull Map<String, Object> args){
        ActivePotion potion = ActivePotion.deserialize(args);
        if(potion instanceof InflictedPotion a){
            a.setInflictor((PotionInflictor) args.getOrDefault("inflictor", null));
            return a;
        }
        return potion;
    }
}
