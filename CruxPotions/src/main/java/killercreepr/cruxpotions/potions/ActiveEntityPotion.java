package killercreepr.cruxpotions.potions;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ActiveEntityPotion<T extends Entity> extends ActivePotion{
    protected final @Nullable T parsedEntity;
    public ActiveEntityPotion(@NotNull Class<T> type, @NotNull CruxPotion potion, @NotNull Entity entity, int duration, int amplifier) {
        super(potion, entity, duration, amplifier);
        if(type.isAssignableFrom(entity.getClass())){
            parsedEntity = type.cast(entity);
        }else parsedEntity = null;
    }

    @Override
    protected void t() {
        super.t();
        if(parsedEntity==null) return;
        tick(parsedEntity);
    }

    @Override
    protected void started() {
        super.started();
        if(parsedEntity==null) return;
        started(parsedEntity);
    }

    @Override
    protected void stopped() {
        super.stopped();
        if(parsedEntity==null) return;
        stopped(parsedEntity);
    }

    protected void tick(@NotNull T e){
    }

    protected void started(@NotNull T e){

    }
    protected void stopped(@NotNull T e){

    }

    @Nullable
    public T getParsedEntity() {
        return parsedEntity;
    }
}
