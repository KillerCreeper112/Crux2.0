package killercreepr.cruxpotions.persistence;

import killercreepr.crux.Crux;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;

public class CustomPotionEffectListTagType implements PersistentDataType<PersistentDataContainer, Collection<CustomPotionHolder>> {
    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<Collection<CustomPotionHolder>> getComplexType() {
        return (Class<Collection<CustomPotionHolder>>) (Class<?>) Collection.class;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull Collection<CustomPotionHolder> complex, @NotNull PersistentDataAdapterContext context) {
        int index = -1;
        PersistentDataContainer c = context.newPersistentDataContainer();
        for(CustomPotionHolder e : complex){
            index++;
            c.set(Crux.key(String.valueOf(index)), CruxPotionPersistence.CUSTOM_POTION_EFFECT, e);
        }
        return c;
    }

    @Override
    public @NotNull Collection<CustomPotionHolder> fromPrimitive(@NotNull PersistentDataContainer c, @NotNull PersistentDataAdapterContext context) {
        Collection<CustomPotionHolder> list = new HashSet<>();
        for(NamespacedKey k : c.getKeys()){
            try{
                list.add(c.get(k, CruxPotionPersistence.CUSTOM_POTION_EFFECT));
            }catch (Exception ex){ ex.printStackTrace(); }
        }
        return list;
    }
}
