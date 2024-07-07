package killercreepr.crux.game;

import org.jetbrains.annotations.NotNull;

public interface Statutable {
    @NotNull GenericStatus getStatus();
    void setStatus(@NotNull GenericStatus status);

    default void setStopped(){
        setStatus(GenericStatus.STOPPED);
    }
    default void setStarted(){
        setStatus(GenericStatus.STARTED);
    }

    default void setIdled(){
        setStatus(GenericStatus.IDLE);
    }

    default boolean isActive(){
        return getStatus() == GenericStatus.STARTED;
    }
}
