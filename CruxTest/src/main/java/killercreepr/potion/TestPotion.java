package killercreepr.potion;

import killercreepr.cruxpotions.potions.ActivePotion;
import killercreepr.cruxpotions.potions.ActivePotionImpl;
import killercreepr.cruxpotions.potions.GenericPotion;
import killercreepr.cruxpotions.potions.inflictor.PotionInflictor;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TestPotion extends GenericPotion {
    public TestPotion() {
        super(Key.key("test:myman"));
    }

    @Override
    public @NotNull ActivePotion create(@NotNull Entity e, int duration, int amplifier, @Nullable PotionInflictor inflictor) {
        return new ActivePotionImpl(this, e, duration, amplifier);
    }
}
