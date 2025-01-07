package killercreepr.cruxform.api.scheduler.context;

public interface ShapeTickContext {
    int getTick();
    int getMaxTicks();
    int getLocationAmount();
    default float getTickProgress(){
        return (float) getTick() / (float) getMaxTicks();
    }
    boolean wasCancelled();
    void setCancelled(boolean value);
}
