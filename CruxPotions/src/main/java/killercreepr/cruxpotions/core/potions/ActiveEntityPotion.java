package killercreepr.cruxpotions.core.potions;

import killercreepr.cruxpotions.api.potion.CruxPotion;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ActiveEntityPotion<T extends Entity> extends SimpleActivePotion {
    protected final @Nullable T parsedEntity;
    public ActiveEntityPotion(@NotNull Class<T> type, @NotNull CruxPotion potion, @NotNull Entity entity, int duration, int amplifier) {
        super(potion, entity, duration, amplifier);
        if(type.isAssignableFrom(entity.getClass())){
            parsedEntity = type.cast(entity);
        }else parsedEntity = null;
    }

    @Override
    protected void onUpdate(int oldDuration, int oldAmplifier) {
        super.onUpdate(oldDuration, oldAmplifier);
        if(parsedEntity==null) return;
        onUpdate(parsedEntity, oldDuration, oldAmplifier);
    }

    protected void onUpdate(@NotNull T e, int oldDuration, int oldAmplifier) {
    }

    @Override
    protected void onTick() {
        super.onTick();
        if(parsedEntity==null) return;
        onTick(parsedEntity);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(parsedEntity==null) return;
        onStart(parsedEntity);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(parsedEntity==null) return;
        onStop(parsedEntity);
    }

    protected void onTick(@NotNull T e){
    }

    protected void onStart(@NotNull T e){

    }
    protected void onStop(@NotNull T e){

    }

    @Nullable
    public T getParsedEntity() {
        return parsedEntity;
    }
}
