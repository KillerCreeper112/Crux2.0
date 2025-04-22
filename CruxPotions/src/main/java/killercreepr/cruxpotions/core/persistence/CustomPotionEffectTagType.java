package killercreepr.cruxpotions.core.persistence;

import killercreepr.crux.core.Crux;
import killercreepr.cruxpotions.api.potion.CruxPotion;
import killercreepr.cruxpotions.api.potion.StoredPotion;
import killercreepr.cruxpotions.core.registries.CruxPotionRegistries;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class CustomPotionEffectTagType implements PersistentDataType<PersistentDataContainer, StoredPotion> {
    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<StoredPotion> getComplexType() {
        return StoredPotion.class;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull StoredPotion complex, @NotNull PersistentDataAdapterContext context) {
        PersistentDataContainer c = context.newPersistentDataContainer();
        c.set(Crux.key("type"), PersistentDataType.STRING, complex.getPotion().key().asString());
        c.set(Crux.key("amplifier"), PersistentDataType.INTEGER, complex.getAmplifier());
        c.set(Crux.key("duration"), PersistentDataType.INTEGER, complex.getDuration());
        /*if(!complex.isAmbient()) c.set(k("ambient"), PersistentDataType.BOOLEAN, complex.isAmbient());
        if(!complex.hasParticles()) c.set(k("particles"), PersistentDataType.BOOLEAN, complex.hasParticles());
        if(!complex.hasIcon()) c.set(k("icon"), PersistentDataType.BOOLEAN, complex.hasIcon());*/
        return c;
    }

    @Override
    public @NotNull StoredPotion fromPrimitive(@NotNull PersistentDataContainer c, @NotNull PersistentDataAdapterContext context) {
        CruxPotion t = CruxPotionRegistries.POTION.get(Crux.key(c.getOrDefault(Crux.key("type"), PersistentDataType.STRING, "a")));
        if(t == null) throw new RuntimeException("Custom potion effect type cannot be NULL!");
        return null;
        /*return new SimpleStoredPotion(t,
                c.getOrDefault(Crux.key("duration"), PersistentDataType.INTEGER, 0),
                c.getOrDefault(Crux.key("amplifier"), PersistentDataType.INTEGER, 0));*/
        /*return new PotionEffect(t, c.getOrDefault(k("duration"), PersistentDataType.INTEGER, 0),
                c.getOrDefault(k("amplifier"), PersistentDataType.INTEGER, 0),
                c.getOrDefault(k("ambient"), PersistentDataType.BOOLEAN, true),
                c.getOrDefault(k("particles"), PersistentDataType.BOOLEAN, true),
                c.getOrDefault(k("icon"), PersistentDataType.BOOLEAN, true));*/
    }
}
