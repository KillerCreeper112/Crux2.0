package killercreepr.crux.core.game;

import killercreepr.crux.api.data.tick.ManagedTicked;
import killercreepr.crux.api.game.Statutable;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleStatutable implements Statutable, ManagedTicked {
    protected @NotNull GenericStatus status = GenericStatus.IDLE;
    @Override
    public @NotNull GenericStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(@NotNull GenericStatus status) {
        if(this.status==status) return;
        this.status = status;

        if(status == GenericStatus.STARTED){
            started();
            return;
        }
        if(status == GenericStatus.STOPPED){
            stopped();
        }
    }

    @Override
    public boolean shouldStop() {
        return !isActive();
    }
}
