package killercreepr.cruxpotions.api.potion;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxpotions.api.potion.inflictor.PotionInflictor;
import killercreepr.cruxpotions.core.persistence.SimpleStoredPotion;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public interface StoredPotion {
    static Builder builder(){
        return new SimpleStoredPotion.Builder();
    }

    static StoredPotion storedPotion(@NotNull CruxPotion potion, int duration, int amplifier, Creator creator){
        return storedPotion(potion, duration, amplifier, creator, null);
    }
    static StoredPotion storedPotion(@NotNull CruxPotion potion, int duration, int amplifier, Creator creator,
                                     Function<FileContext<?>,FileObject> serializer){
        return new SimpleStoredPotion(potion, duration, amplifier, creator, serializer);
    }
    static StoredPotion storedPotion(@NotNull CruxPotion potion, int duration, int amplifier){
        return storedPotion(potion, duration, amplifier, null, null);
    }

    @NotNull ActivePotion create(@NotNull Entity e);
    @NotNull ActivePotion create(@NotNull Entity e, @Nullable PotionInflictor inflictor);
    @NotNull CruxPotion getPotion();
    int getDuration();
    int getAmplifier();
    @Contract(pure = true)
    @NotNull StoredPotion withDuration(int duration);
    @Contract(pure = true)
    @NotNull StoredPotion withAmplifier(int amplifier);
    @Contract(pure = true)
    @NotNull StoredPotion withPotion(@NotNull CruxPotion potion);

    @Nullable FileObject serializeDataToFile(FileContext<?> ctx);

    interface Builder{
        Builder potion(CruxPotion potion);
        Builder duration(int duration);
        Builder amplifier(int amplifier);
        Builder creator(Creator creator);
        Builder fileSerializer(Function<FileContext<?>,FileObject> serializer);
        StoredPotion build();
    }

    interface Creator{
        @NotNull ActivePotion create(@NotNull Entity e, @Nullable PotionInflictor inflictor);
    }
}
