package killercreepr.cruxworlds.core.world.event;

import killercreepr.cruxworlds.api.world.event.WorldEvent;

import java.util.Collection;

public class MultiWorldEvent implements WorldEvent {
    protected final Collection<WorldEvent> events;

    public MultiWorldEvent(Collection<WorldEvent> events) {
        this.events = events;
    }

    @Override
    public void tick() {
        events.removeIf(event ->{
            if(event.shouldStop()) return true;
            event.tick();
            return false;
        });
    }

    @Override
    public void started() {
        events.forEach(WorldEvent::started);
    }

    @Override
    public void stopped() {
        events.forEach(WorldEvent::stopped);
    }

    @Override
    public boolean shouldStop() {
        return events.isEmpty();
    }
}
