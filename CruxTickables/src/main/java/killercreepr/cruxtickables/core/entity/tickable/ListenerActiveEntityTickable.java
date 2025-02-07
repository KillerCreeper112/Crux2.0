package killercreepr.cruxtickables.core.entity.tickable;

import killercreepr.crux.core.Crux;
import killercreepr.cruxattributes.api.equipment.CruxSlot;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickable;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class ListenerActiveEntityTickable extends SimpleActiveEntityTickable implements Listener {
    public ListenerActiveEntityTickable(Entity entity, EntityTickable tickable, CruxSlot slot) {
        super(entity, tickable, slot);
    }

    protected void enableListener(){
        Crux.getServer().getPluginManager().registerEvents(this, Crux.getMainPlugin());
    }

    protected void disableListener(){
        HandlerList.unregisterAll(this);
    }

    @Override
    public void started() {
        enableListener();
    }

    @Override
    public void stopped() {
        disableListener();
    }
}
