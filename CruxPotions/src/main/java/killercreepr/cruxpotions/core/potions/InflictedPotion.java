package killercreepr.cruxpotions.core.potions;

import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxpotions.api.potion.CruxPotion;
import killercreepr.cruxpotions.api.potion.StoredPotion;
import killercreepr.cruxpotions.api.potion.inflictor.PotionInflictor;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InflictedPotion extends SimpleActivePotion {
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
    public @NotNull StoredPotion store() {
        return StoredPotion.storedPotion(potion, duration, amplifier, (e, inflictor) ->{
            return new InflictedPotion(potion, e, duration, amplifier, inflictor);
        }, ctx ->{
            if(inflictor == null) return null;
            var reg = ctx.getRegistry();
            return new FileObject().add("inflictor", reg.serializeToFile(inflictor));
        });
    }
}
