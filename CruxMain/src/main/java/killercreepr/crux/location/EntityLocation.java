package killercreepr.crux.location;

import killercreepr.crux.data.Holder;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityLocation extends DynamicLocation{
    public static @NotNull EntityLocation from(@NotNull Entity e){
        return new EntityLocation(e);
    }

    protected final @NotNull Holder<Entity> entity;
    public EntityLocation(@NotNull Entity e, @Nullable DynamicInfo info){
        super(e.getLocation(), info);
        entity = Holder.weakReference(e);
    }

    public EntityLocation(@NotNull Entity e){
        this(e, null);
    }

    @Override
    public @NotNull Location value() {
        Entity entity = this.entity.value();
        Location l = entity != null ? entity.getLocation() : this.clone();
        if(info != null) return info.apply(l);
        return l;
    }

    public @NotNull Holder<Entity> getEntity() {
        return entity;
    }
}
