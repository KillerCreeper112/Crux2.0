package killercreepr.crux.core.persistence.type;

import killercreepr.crux.core.Crux;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class PotionEffectTagType implements PersistentDataType<PersistentDataContainer, PotionEffect> {
    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<PotionEffect> getComplexType() {
        return PotionEffect.class;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull PotionEffect complex, @NotNull PersistentDataAdapterContext context) {
        PersistentDataContainer c = context.newPersistentDataContainer();
        c.set(Crux.key("type"), PersistentDataType.STRING, complex.getType().getKey().asString());
        c.set(Crux.key("amplifier"), PersistentDataType.INTEGER, complex.getAmplifier());
        c.set(Crux.key("duration"), PersistentDataType.INTEGER, complex.getDuration());
        if(!complex.isAmbient()) c.set(Crux.key("ambient"), PersistentDataType.BOOLEAN, complex.isAmbient());
        if(!complex.hasParticles()) c.set(Crux.key("particles"), PersistentDataType.BOOLEAN, complex.hasParticles());
        if(!complex.hasIcon()) c.set(Crux.key("icon"), PersistentDataType.BOOLEAN, complex.hasIcon());
        return c;
    }

    @Override
    public @NotNull PotionEffect fromPrimitive(@NotNull PersistentDataContainer c, @NotNull PersistentDataAdapterContext context) {
        PotionEffectType t = PotionEffectType.getByKey(Crux.key(c.getOrDefault(Crux.key("type"), PersistentDataType.STRING, "a")));
        if(t == null) throw new RuntimeException("Potion effect type cannot be NULL!");
        return new PotionEffect(t, c.getOrDefault(Crux.key("duration"), PersistentDataType.INTEGER, 0),
                c.getOrDefault(Crux.key("amplifier"), PersistentDataType.INTEGER, 0),
                c.getOrDefault(Crux.key("ambient"), PersistentDataType.BOOLEAN, true),
                c.getOrDefault(Crux.key("particles"), PersistentDataType.BOOLEAN, true),
                c.getOrDefault(Crux.key("icon"), PersistentDataType.BOOLEAN, true));
    }
}
