package killercreepr.crux.persistence.impl;

import killercreepr.crux.Crux;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;

public class PotionEffectListTagType implements PersistentDataType<PersistentDataContainer, Collection<PotionEffect>> {
    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<Collection<PotionEffect>> getComplexType() {
        return (Class<Collection<PotionEffect>>) (Class<?>) Collection.class;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull Collection<PotionEffect> complex, @NotNull PersistentDataAdapterContext context) {
        int index = -1;
        PersistentDataContainer c = context.newPersistentDataContainer();
        for(PotionEffect e : complex){
            index++;
            c.set(Crux.key(String.valueOf(index)), PotionEffectTagType.POTION_EFFECT, e);
        }
        return c;
    }

    @Override
    public @NotNull Collection<PotionEffect> fromPrimitive(@NotNull PersistentDataContainer c, @NotNull PersistentDataAdapterContext context) {
        Collection<PotionEffect> list = new HashSet<>();
        for(NamespacedKey k : c.getKeys()){
            try{
                list.add(c.get(k, PotionEffectTagType.POTION_EFFECT));
            }catch (Exception ex){ ex.printStackTrace(); }
        }
        return list;
    }
}
