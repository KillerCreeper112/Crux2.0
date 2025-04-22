package killercreepr.cruxpotions.core.persistence;

import killercreepr.crux.core.Crux;
import killercreepr.cruxpotions.api.potion.StoredPotion;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;

public class CustomPotionEffectListTagType implements PersistentDataType<PersistentDataContainer, Collection<StoredPotion>> {
    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<Collection<StoredPotion>> getComplexType() {
        return (Class<Collection<StoredPotion>>) (Class<?>) Collection.class;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull Collection<StoredPotion> complex, @NotNull PersistentDataAdapterContext context) {
        int index = -1;
        PersistentDataContainer c = context.newPersistentDataContainer();
        for(StoredPotion e : complex){
            index++;
            c.set(Crux.key(String.valueOf(index)), CruxPotionsPersistence.CUSTOM_POTION_EFFECT, e);
        }
        return c;
    }

    @Override
    public @NotNull Collection<StoredPotion> fromPrimitive(@NotNull PersistentDataContainer c, @NotNull PersistentDataAdapterContext context) {
        Collection<StoredPotion> list = new HashSet<>();
        for(NamespacedKey k : c.getKeys()){
            try{
                list.add(c.get(k, CruxPotionsPersistence.CUSTOM_POTION_EFFECT));
            }catch (Exception ex){ ex.printStackTrace(); }
        }
        return list;
    }
}
